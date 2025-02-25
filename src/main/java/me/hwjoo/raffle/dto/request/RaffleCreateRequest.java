package me.hwjoo.raffle.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hwjoo.raffle.domain.Raffle;
import me.hwjoo.raffle.domain.RafflePrize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleCreateRequest {
    
    @NotBlank(message = "제목은 필수입니다")
    private String title;
    
    private String description;
    
    private String imageUrl;
    
    @NotNull(message = "시작 날짜는 필수입니다")
    private LocalDateTime startDate;
    
    @NotNull(message = "종료 날짜는 필수입니다")
    @Future(message = "종료 날짜는 미래 시간이어야 합니다")
    private LocalDateTime endDate;
    
    @NotNull(message = "추첨 날짜는 필수입니다")
    @Future(message = "추첨 날짜는 미래 시간이어야 합니다")
    private LocalDateTime drawDate;
    
    @Min(value = 1, message = "최대 참여자 수는 1명 이상이어야 합니다")
    private int maxParticipants;
    
    @Min(value = 1, message = "당첨자 수는 1명 이상이어야 합니다")
    private int numberOfWinners;
    
    @Valid
    @Builder.Default
    private List<RafflePrizeRequest> prizes = new ArrayList<>();
    
    // 도메인 객체로 변환하는 메서드
    public Raffle toEntity() {
        return Raffle.builder()
                .title(this.title)
                .description(this.description)
                .imageUrl(this.imageUrl)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .drawDate(this.drawDate)
                .maxParticipants(this.maxParticipants)
                .numberOfWinners(this.numberOfWinners)
                .build();
    }
    
    // 경품 DTO를 엔티티로 변환
    public List<RafflePrize> toPrizeEntities() {
        if (this.prizes == null) {
            return List.of();
        }
        
        return this.prizes.stream()
                .map(RafflePrizeRequest::toEntity)
                .collect(Collectors.toList());
    }
} 