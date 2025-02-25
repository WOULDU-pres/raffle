package me.hwjoo.raffle.dto.response;

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
public class RaffleWinnerResponse {
    private int rank;
    private String prizeName;
    private WinnerInfo winner;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WinnerInfo {
        private String entryNumber;
        private String userName;
    }
    
    public static RaffleWinnerResponse from(RaffleParticipant participant, int rank) {
        return RaffleWinnerResponse.builder()
                .rank(rank)
                .prizeName(participant.getPrize().getName())
                .winner(WinnerInfo.builder()
                        .entryNumber(participant.getId().toString())
                        .userName(maskUUID(participant.getMemberId()))
                        .build())
                .build();
    }
    
    private static String maskUUID(UUID uuid) {
        String uuidStr = uuid.toString();
        return uuidStr.substring(0, 4) + "..." + uuidStr.substring(uuidStr.length() - 4);
    }
} 