package me.hwjoo.raffle.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hwjoo.raffle.domain.RaffleStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleUpdateRequest {
    
    @NotBlank(message = "제목은 필수입니다")
    private String title;
    
    @NotBlank(message = "설명은 필수입니다")
    private String description;
    
    private String imageUrl;
    
    @NotNull(message = "시작 날짜는 필수입니다")
    private LocalDateTime startDate;
    
    @NotNull(message = "종료 날짜는 필수입니다")
    private LocalDateTime endDate;
    
    @NotNull(message = "추첨 날짜는 필수입니다")
    private LocalDateTime drawDate;
    
    @Min(value = 1, message = "최대 참여자 수는 1명 이상이어야 합니다")
    private int maxParticipants;
    
    @Min(value = 1, message = "당첨자 수는 1명 이상이어야 합니다")
    private int numberOfWinners;
    
    private RaffleStatus status;
    
    @Valid
    @Size(min = 1, message = "최소 1개 이상의 경품이 필요합니다")
    private List<RafflePrizeRequest> prizes;
} 