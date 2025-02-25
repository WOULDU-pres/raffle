package me.hwjoo.raffle.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hwjoo.raffle.domain.RaffleParticipant;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleParticipateRequest {
    
    @NotNull(message = "회원 ID는 필수입니다")
    private UUID memberId;
    
    // 도메인 객체로 변환하는 메서드
    public RaffleParticipant toEntity() {
        return RaffleParticipant.builder()
                .memberId(this.memberId)
                .build();
    }
} 