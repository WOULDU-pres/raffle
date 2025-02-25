package me.hwjoo.raffle.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "raffles")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Raffle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private LocalDateTime drawDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RaffleStatus status;

    @Column(nullable = false)
    private int maxParticipants;

    @Column(nullable = false)
    private int numberOfWinners;

    @OneToMany(mappedBy = "raffle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RafflePrize> prizes = new ArrayList<>();

    @OneToMany(mappedBy = "raffle", cascade = CascadeType.ALL)
    private List<RaffleParticipant> participants = new ArrayList<>();

    @Builder
    public Raffle(String title, String description, String imageUrl, 
                 LocalDateTime startDate, LocalDateTime endDate, LocalDateTime drawDate,
                 int maxParticipants, int numberOfWinners) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.drawDate = drawDate;
        this.maxParticipants = maxParticipants;
        this.numberOfWinners = numberOfWinners;
        this.status = determineInitialStatus(startDate);
    }

    // 상태 결정 메서드
    private RaffleStatus determineInitialStatus(LocalDateTime startDate) {
        return LocalDateTime.now().isBefore(startDate) 
               ? RaffleStatus.SCHEDULED 
               : RaffleStatus.ACTIVE;
    }

    // 상태 업데이트 메서드
    public void updateStatus() {
        LocalDateTime now = LocalDateTime.now();
        
        if (this.status == RaffleStatus.SCHEDULED && now.isAfter(this.startDate)) {
            this.status = RaffleStatus.ACTIVE;
        } else if (this.status == RaffleStatus.ACTIVE && now.isAfter(this.endDate)) {
            this.status = RaffleStatus.CLOSED;
        }
    }

    // 참여자 추가 메서드
    public void addParticipant(RaffleParticipant participant) {
        this.participants.add(participant);
        participant.setRaffle(this);
    }

    // 경품 추가 메서드
    public void addPrize(RafflePrize prize) {
        this.prizes.add(prize);
        prize.setRaffle(this);
    }

    // 현재 참여자 수 조회
    public int getCurrentParticipants() {
        return this.participants.size();
    }

    // 상태 설정 메서드
    public void setStatus(RaffleStatus status) {
        this.status = status;
    }

    // 제목 설정
    public void setTitle(String title) {
        this.title = title;
    }

    // 설명 설정
    public void setDescription(String description) {
        this.description = description;
    }

    // 이미지 URL 설정
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // 시작 날짜 설정
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    // 종료 날짜 설정
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    // 추첨 날짜 설정
    public void setDrawDate(LocalDateTime drawDate) {
        this.drawDate = drawDate;
    }

    // 최대 참여자 수 설정
    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    // 당첨자 수 설정
    public void setNumberOfWinners(int numberOfWinners) {
        this.numberOfWinners = numberOfWinners;
    }
} 