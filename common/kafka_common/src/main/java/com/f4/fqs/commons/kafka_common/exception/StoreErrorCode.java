package com.f4.fqs.commons.kafka_common.exception;

import com.f4.fqs.commons.domain.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "COMMON_0001", "잘못된 입력 값입니다."),
    INTERNAL_SERVER_ERROR(500, "COMMON_002", "서버 에러입니다."),

    // Security
    FAILED_SEND_EVENT(401, "STORE_001", "이벤트 전송에 실패했습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
