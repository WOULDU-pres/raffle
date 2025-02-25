package me.hwjoo.raffle.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "raffle_participants", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"raffle_id", "member_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RaffleParticipant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raffle_id")
    private Raffle raffle;

    @Column(nullable = false)
    private boolean isWinner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prize_id")
    private RafflePrize prize;

    @Builder
    public RaffleParticipant(UUID memberId) {
        this.memberId = memberId;
        this.isWinner = false;
    }

    // 연관관계 설정 메서드
    public void setRaffle(Raffle raffle) {
        this.raffle = raffle;
    }

    // 당첨 처리 메서드
    public void markAsWinner(RafflePrize prize) {
        this.isWinner = true;
        this.prize = prize;
    }
} 