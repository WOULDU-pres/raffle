package me.hwjoo.raffle.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hwjoo.raffle.domain.RafflePrize;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RafflePrizeResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private int quantity;
    private int rank;
    
    public static RafflePrizeResponse from(RafflePrize prize) {
        return RafflePrizeResponse.builder()
                .id(prize.getId())
                .name(prize.getName())
                .description(prize.getDescription())
                .imageUrl(prize.getImageUrl())
                .quantity(prize.getQuantity())
                .rank(prize.getRank())
                .build();
    }
} 