package me.hwjoo.raffle.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.hwjoo.raffle.dto.ApiResponse;
import me.hwjoo.raffle.dto.request.RaffleCreateRequest;
import me.hwjoo.raffle.dto.request.RaffleParticipateRequest;
import me.hwjoo.raffle.dto.request.RaffleUpdateRequest;
import me.hwjoo.raffle.dto.response.PageResponse;
import me.hwjoo.raffle.dto.response.RaffleListResponse;
import me.hwjoo.raffle.dto.response.RaffleResponse;
import me.hwjoo.raffle.dto.response.RaffleWinnerResponse;
import me.hwjoo.raffle.service.RaffleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("/api/raffles")
@RequiredArgsConstructor
public class RaffleController {
    
    private final RaffleService raffleService;
    
    private static final Logger log = LoggerFactory.getLogger(RaffleController.class);
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RaffleResponse> createRaffle(@Valid @RequestBody RaffleCreateRequest request) {
        return ApiResponse.success(raffleService.createRaffle(request));
    }
    
    @GetMapping
    public ApiResponse<PageResponse<RaffleListResponse>> getRaffles(@PageableDefault(size = 10) Pageable pageable) {
        PageResponse<RaffleListResponse> response = raffleService.getRaffles(pageable);
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String jsonResponse = mapper.writeValueAsString(response);
            log.info("Raffle list response JSON: {}", jsonResponse);
            
            // 추가 디버깅: 각 래플의 ID도 개별적으로 로깅
            for (RaffleListResponse raffle : response.getContent()) {
                log.info("Raffle ID: {}, Type: {}", raffle.getId(), raffle.getId().getClass().getName());
            }
        } catch (Exception e) {
            log.error("Failed to serialize response", e);
        }
        
        return ApiResponse.success(response);
    }
    
    @GetMapping("/{raffleId}")
    public ApiResponse<RaffleResponse> getRaffle(@PathVariable("raffleId") Long raffleId) {
        return ApiResponse.success(raffleService.getRaffle(raffleId));
    }
    
    @PostMapping("/{raffleId}/participate")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> participateInRaffle(
            @PathVariable("raffleId") Long raffleId,
            @RequestHeader(value = "X-Member-Id", required = false) UUID memberId,
            @Valid @RequestBody(required = false) RaffleParticipateRequest request) {
        
        // X-Member-Id 헤더가 있으면 그 값을 사용, 없으면 요청 본문의 memberId 사용
        RaffleParticipateRequest participateRequest = request;
        if (memberId != null) {
            participateRequest = RaffleParticipateRequest.builder()
                    .memberId(memberId)
                    .build();
        }
        
        raffleService.participateInRaffle(raffleId, participateRequest);
        return ApiResponse.success(null);
    }
    
    @GetMapping("/{raffleId}/winners")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<RaffleWinnerResponse>> getRaffleWinners(@PathVariable("raffleId") Long raffleId) {
        return ApiResponse.success(raffleService.getRaffleWinners(raffleId));
    }
    
    @PostMapping("/{raffleId}/draw")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> drawRaffleWinners(@PathVariable("raffleId") Long raffleId) {
        raffleService.drawRaffleWinners(raffleId);
        return ApiResponse.success(null);
    }
    
    @PutMapping("/{raffleId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<RaffleResponse> updateRaffle(
            @PathVariable("raffleId") Long raffleId,
            @Valid @RequestBody RaffleUpdateRequest request) {
        return ApiResponse.success(raffleService.updateRaffle(raffleId, request));
    }
    
    @DeleteMapping("/{raffleId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> deleteRaffle(@PathVariable("raffleId") Long raffleId) {
        raffleService.deleteRaffle(raffleId);
        return ApiResponse.success(null);
    }
} 