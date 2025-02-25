package me.hwjoo.raffle.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hwjoo.raffle.domain.RafflePrize;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RafflePrizeRequest {
    
    @NotBlank(message = "경품 이름은 필수입니다")
    private String name;
    
    private String description;
    
    private String imageUrl;
    
    @Min(value = 1, message = "경품 수량은 1개 이상이어야 합니다")
    private int quantity;
    
    // 도메인 객체로 변환하는 메서드
    public RafflePrize toEntity() {
        return RafflePrize.builder()
                .name(this.name)
                .description(this.description)
                .imageUrl(this.imageUrl)
                .quantity(this.quantity)
                .build();
    }
} 