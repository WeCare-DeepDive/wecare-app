package com.example.wecare.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GeneralResponseCode implements ApiResponseCode {
    DUPLICATED_RELATIONSHIP(HttpStatus.CONFLICT, "중복된 연결입니다."),
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "중복된 계정입니다."),
    RECURSIVE_INVITATION_CODE(HttpStatus.BAD_REQUEST, "자신의 초대코드는 사용할 수 없습니다."),
    RECURSIVE_INVITATION_ROLE(HttpStatus.BAD_REQUEST, "동일한 역할의 사용자는 연결될 수 없습니다."),
    CONNECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 연결을 찾을 수 없습니다."),
    INVITER_NOT_FOUND(HttpStatus.NOT_FOUND, "초대자에 해당하는 사용자를 찾을 수 없습니다."),
    INVITATION_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "초대 코드를 찾을 수 없습니다."),
    DEPENDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "피보호자를 찾을 수 없습니다."),
    ROUTINE_NOT_FOUND(HttpStatus.NOT_FOUND, "루틴을 찾을 수 없습니다."),
    HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "수행 기록을 찾을 수 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "부적절한 접근입니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "해당 리소스에 접근할 권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 알 수 없는 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}