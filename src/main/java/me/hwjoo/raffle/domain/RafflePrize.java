package me.hwjoo.raffle.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "raffle_prizes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RafflePrize extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String imageUrl;
    
    private int rank;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raffle_id")
    private Raffle raffle;

    @Builder
    public RafflePrize(String name, String description, String imageUrl, int quantity) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    // 연관관계 설정 메서드
    public void setRaffle(Raffle raffle) {
        this.raffle = raffle;
    }
} 