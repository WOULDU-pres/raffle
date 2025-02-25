package me.hwjoo.raffle.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hwjoo.raffle.domain.Raffle;
import me.hwjoo.raffle.domain.RaffleStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleListResponse {
    private Long id;
    private String title;
    private String imageUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime drawDate;
    private RaffleStatus status;
    private int maxParticipants;
    private int currentParticipants;
    
    public static RaffleListResponse from(Raffle raffle) {
        return RaffleListResponse.builder()
                .id(raffle.getId())
                .title(raffle.getTitle())
                .imageUrl(raffle.getImageUrl())
                .startDate(raffle.getStartDate())
                .endDate(raffle.getEndDate())
                .drawDate(raffle.getDrawDate())
                .status(raffle.getStatus())
                .maxParticipants(raffle.getMaxParticipants())
                .currentParticipants(raffle.getCurrentParticipants())
                .build();
    }
} 