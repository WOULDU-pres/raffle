package me.hwjoo.raffle.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hwjoo.raffle.domain.Raffle;
import me.hwjoo.raffle.domain.RaffleStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime drawDate;
    private RaffleStatus status;
    private int maxParticipants;
    private int currentParticipants;
    private int numberOfWinners;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RafflePrizeResponse> prizes;
    
    public static RaffleResponse from(Raffle raffle) {
        return RaffleResponse.builder()
                .id(raffle.getId())
                .title(raffle.getTitle())
                .description(raffle.getDescription())
                .imageUrl(raffle.getImageUrl())
                .startDate(raffle.getStartDate())
                .endDate(raffle.getEndDate())
                .drawDate(raffle.getDrawDate())
                .status(raffle.getStatus())
                .maxParticipants(raffle.getMaxParticipants())
                .currentParticipants(raffle.getCurrentParticipants())
                .numberOfWinners(raffle.getNumberOfWinners())
                .createdAt(raffle.getCreatedAt())
                .updatedAt(raffle.getUpdatedAt())
                .prizes(raffle.getPrizes().stream()
                        .map(RafflePrizeResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
} 