package me.hwjoo.raffle.service;

import lombok.RequiredArgsConstructor;
import me.hwjoo.raffle.domain.Raffle;
import me.hwjoo.raffle.domain.RaffleParticipant;
import me.hwjoo.raffle.domain.RafflePrize;
import me.hwjoo.raffle.domain.RaffleStatus;
import me.hwjoo.raffle.dto.request.RaffleCreateRequest;
import me.hwjoo.raffle.dto.request.RaffleParticipateRequest;
import me.hwjoo.raffle.dto.request.RafflePrizeRequest;
import me.hwjoo.raffle.dto.request.RaffleUpdateRequest;
import me.hwjoo.raffle.dto.response.RaffleListResponse;
import me.hwjoo.raffle.dto.response.RaffleResponse;
import me.hwjoo.raffle.dto.response.RaffleWinnerResponse;
import me.hwjoo.raffle.dto.response.PageResponse;
import me.hwjoo.raffle.exception.BusinessException;
import me.hwjoo.raffle.exception.ErrorCode;
import me.hwjoo.raffle.repository.RaffleParticipantRepository;
import me.hwjoo.raffle.repository.RaffleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RaffleService {
    
    private final RaffleRepository raffleRepository;
    private final RaffleParticipantRepository participantRepository;
    
    @Transactional
    public RaffleResponse createRaffle(RaffleCreateRequest request) {
        // DTO의 매퍼 메서드 활용
        Raffle raffle = request.toEntity();
        
        // 경품 추가 (DTO의 매퍼 메서드 활용)
        List<RafflePrize> prizes = request.toPrizeEntities();
        for (RafflePrize prize : prizes) {
            raffle.addPrize(prize);
        }
        
        Raffle savedRaffle = raffleRepository.save(raffle);
        return RaffleResponse.from(savedRaffle);
    }
    
    @Transactional(readOnly = true)
    public RaffleResponse getRaffle(Long raffleId) {
        Raffle raffle = findRaffleById(raffleId);
        return RaffleResponse.from(raffle);
    }
    
    @Transactional(readOnly = true)
    public PageResponse<RaffleListResponse> getRaffles(Pageable pageable) {
        Page<Raffle> raffles = raffleRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<RaffleListResponse> content = raffles.stream()
                .map(RaffleListResponse::from)
                .collect(Collectors.toList());
        
        return PageResponse.<RaffleListResponse>builder()
                .content(content)
                .pageNumber(raffles.getNumber())
                .pageSize(raffles.getSize())
                .totalElements(raffles.getTotalElements())
                .totalPages(raffles.getTotalPages())
                .first(raffles.isFirst())
                .last(raffles.isLast())
                .build();
    }
    
    @Transactional
    public void participateInRaffle(Long raffleId, RaffleParticipateRequest request) {
        Raffle raffle = findRaffleById(raffleId);
        
        // 래플 상태 확인
        if (raffle.getStatus() != RaffleStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.RAFFLE_CLOSED);
        }
        
        // 이미 참여했는지 확인
        if (participantRepository.existsByRaffleIdAndMemberId(raffleId, request.getMemberId())) {
            throw new BusinessException(ErrorCode.RAFFLE_ALREADY_PARTICIPATED);
        }
        
        // 최대 참여자 수 확인
        int currentParticipants = participantRepository.countParticipantsByRaffleId(raffleId);
        if (currentParticipants >= raffle.getMaxParticipants()) {
            throw new BusinessException(ErrorCode.RAFFLE_MAX_PARTICIPANTS_REACHED);
        }
        
        // DTO의 매퍼 메서드 활용
        RaffleParticipant participant = request.toEntity();
        
        raffle.addParticipant(participant);
        participantRepository.save(participant);
    }
    
    @Transactional(readOnly = true)
    public List<RaffleWinnerResponse> getRaffleWinners(Long raffleId) {
        Raffle raffle = findRaffleById(raffleId);
        
        if (raffle.getStatus() != RaffleStatus.COMPLETED) {
            return new ArrayList<>();
        }
        
        List<RaffleParticipant> winners = participantRepository.findByRaffleAndIsWinnerTrue(raffle);
        
        List<RaffleWinnerResponse> winnerResponses = new ArrayList<>();
        int rank = 1;
        for (RaffleParticipant winner : winners) {
            winnerResponses.add(RaffleWinnerResponse.from(winner, rank++));
        }
        
        return winnerResponses;
    }
    
    @Transactional
    public void drawRaffleWinners(Long raffleId) {
        Raffle raffle = findRaffleById(raffleId);
        
        if (raffle.getStatus() != RaffleStatus.CLOSED) {
            throw new BusinessException(ErrorCode.RAFFLE_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        
        int winnersCount = raffle.getNumberOfWinners();
        List<RaffleParticipant> randomParticipants = participantRepository.findRandomParticipants(raffleId, winnersCount);
        
        if (randomParticipants.isEmpty()) {
            return;
        }
        
        // 당첨자 선정
        List<RafflePrize> prizes = raffle.getPrizes();
        int prizeIndex = 0;
        
        for (RaffleParticipant participant : randomParticipants) {
            if (prizeIndex < prizes.size()) {
                participant.markAsWinner(prizes.get(prizeIndex++));
                participantRepository.save(participant);
            }
        }
        
        // 래플 상태 업데이트
        raffle.setStatus(RaffleStatus.COMPLETED);
        raffleRepository.save(raffle);
    }
    
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    @Transactional
    public void updateRaffleStatuses() {
        LocalDateTime now = LocalDateTime.now();
        
        // SCHEDULED -> ACTIVE
        List<Raffle> scheduledRaffles = raffleRepository.findScheduledRafflesToActivate(now);
        for (Raffle raffle : scheduledRaffles) {
            raffle.setStatus(RaffleStatus.ACTIVE);
            raffleRepository.save(raffle);
        }
        
        // ACTIVE -> CLOSED
        List<Raffle> activeRaffles = raffleRepository.findActiveRafflesToClose(now);
        for (Raffle raffle : activeRaffles) {
            raffle.setStatus(RaffleStatus.CLOSED);
            raffleRepository.save(raffle);
        }
        
        // CLOSED -> COMPLETED (자동 추첨)
        List<Raffle> closedRaffles = raffleRepository.findClosedRafflesToDraw(now);
        for (Raffle raffle : closedRaffles) {
            drawRaffleWinners(raffle.getId());
        }
    }
    
    @Transactional
    public RaffleResponse updateRaffle(Long raffleId, RaffleUpdateRequest request) {
        Raffle raffle = findRaffleById(raffleId);
        
        // 기본 정보 업데이트
        raffle.setTitle(request.getTitle());
        raffle.setDescription(request.getDescription());
        raffle.setImageUrl(request.getImageUrl());
        raffle.setStartDate(request.getStartDate());
        raffle.setEndDate(request.getEndDate());
        raffle.setDrawDate(request.getDrawDate());
        raffle.setMaxParticipants(request.getMaxParticipants());
        raffle.setNumberOfWinners(request.getNumberOfWinners());
        
        // 상태 업데이트 (요청에 상태가 포함된 경우)
        if (request.getStatus() != null) {
            raffle.setStatus(request.getStatus());
        }
        
        // 기존 경품 삭제 및 새 경품 추가
        raffle.getPrizes().clear();
        List<RafflePrize> prizes = request.getPrizes().stream()
                .map(RafflePrizeRequest::toEntity)
                .collect(Collectors.toList());
        
        for (RafflePrize prize : prizes) {
            raffle.addPrize(prize);
        }
        
        Raffle updatedRaffle = raffleRepository.save(raffle);
        return RaffleResponse.from(updatedRaffle);
    }
    
    @Transactional
    public void deleteRaffle(Long raffleId) {
        Raffle raffle = findRaffleById(raffleId);
                
        // 래플 삭제
        raffleRepository.delete(raffle);
    }
    
    private Raffle findRaffleById(Long raffleId) {
        return raffleRepository.findById(raffleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RAFFLE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }
} 