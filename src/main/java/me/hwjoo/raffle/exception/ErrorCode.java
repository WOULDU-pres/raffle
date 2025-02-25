package me.hwjoo.raffle.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_INPUT("ERR-001", "잘못된 입력값"),
    UNAUTHORIZED("ERR-002", "인증 실패"),
    RAFFLE_NOT_FOUND("ERR-101", "래플을 찾을 수 없습니다"),
    RAFFLE_CLOSED("ERR-102", "종료된 래플입니다"),
    RAFFLE_ALREADY_PARTICIPATED("ERR-103", "이미 참여한 래플입니다"),
    RAFFLE_MAX_PARTICIPANTS_REACHED("ERR-104", "최대 참여자 수에 도달했습니다"),
    RAFFLE_ALREADY_STARTED("ERR-104", "이미 시작된 래플은 수정/삭제할 수 없습니다", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
        this.httpStatus = null;
    }

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
} 