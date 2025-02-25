package me.hwjoo.raffle.domain;

public enum RaffleStatus {
    DRAFT,      // 작성 중
    SCHEDULED,  // 예약됨
    ACTIVE,     // 진행 중
    CLOSED,     // 종료됨
    COMPLETED   // 추첨 완료
} 