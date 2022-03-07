package com.naturemobility.seoul.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    // 1000 : 요청 성공
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
//    SUCCESS_READ_USERS(true, 1010, "회원 전체 정보 조회에 성공하였습니다."),
//    SUCCESS_READ_USER(true, 1011, "회원 정보 조회에 성공하였습니다."),
//    SUCCESS_POST_USER(true, 1012, "회원가입에 성공하였습니다."),
//    SUCCESS_LOGIN(true, 1013, "로그인에 성공하였습니다."),
//    SUCCESS_JWT(true, 1014, "JWT 검증에 성공하였습니다."),
//    SUCCESS_DELETE_USER(true, 1015, "회원 탈퇴에 성공하였습니다."),
//    SUCCESS_PATCH_USER(true, 1016, "회원정보 수정에 성공하였습니다."),
//    SUCCESS_READ_SEARCH_USERS(true, 1017, "회원 검색 조회에 성공하였습니다."),
//    SUCCESS_CHECK_EMAIL(true, 1018, "이메일 중복 확인에 성공했습니다."),
//    SUCCESS_CHECK_ID(true, 1019, "아이디 중복 확인에 성공했습니다."),
//    SUCCESS_CHECK_PW(true, 1020, "비밀번호 확인에 성공했습니다."),
//    SUCCESS_EDIT_PW(true, 1021, "비밀번호 변경에 성공했습니다."),

    // 2000 : Request 오류
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2010, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2011, "유효하지 않은 JWT입니다."),
    INVALID_EMAIL(false, 2020, "이메일 형식을 확인해주세요."),
    WRONG_PASSWORD(false, 2030, "비밀번호가 틀렸습니다. 비밀번호를 다시 입력해주세요."),
    DO_NOT_MATCH_PASSWORD(false, 2031, "비밀번호와 비밀번호확인 값이 일치하지 않습니다."),
    NEED_LOGIN(false, 2040, "로그인이 필요합니다"),
    NO_AUTHORITY(false, 2032, "권한이 없습니다."),

    RESERVATION_DUPLICATION(false, 2033, "중복된 예약입니다."),
    DUPLICATION_CALCULATION(false, 2034, "중복된 정산 입니다."),
//    EMPTY_EMAIL(false, 2020, "이메일을 입력해주세요."),
//    EMPTY_PASSWORD(false, 2030, "비밀번호를 입력해주세요."),
//    EMPTY_CONFIRM_PASSWORD(false, 2031, "비밀번호 확인을 입력해주세요."),
//    EMPTY_USERNICKNAME(false, 2040, "닉네임을 입력해주세요."),
//    EMPTY_USERID(false, 2050, "아이디를 입력해주세요."),
//    EMPTY_USERNAME(false, 2060, "이름을 입력해주세요."),

    // 3000 : Response 오류
    RESPONSE_ERROR(false, 3000, "값을 불러오거나 수정하는 데 실패하였습니다."),
    NOT_FOUND_USER(false, 3010, "존재하지 않는 회원입니다."),
    DUPLICATED_USER(false, 3011, "이미 존재하는 회원입니다."),
    DUPLICATED_EMAIL(false, 3012, "이미 존재하는 이메일입니다."),
    DUPLICATED_ID(false, 3013, "이미 존재하는 아이디입니다."),
    INACTIVE_ID(false, 3014, "비활성화된 계정입니다."),
    NOT_FOUND_DATA(false, 3015, "데이터를 찾을 수 없습니다"),
    PAYMENT_CANCEL(false, 3020, "결제가 취소되었습니다."),
    PAYMENT_FAILED(false, 3021, "결제에 실패했습니다"),
    ALREADY_REFUND(false,3030,"이미 전액환불된 주문입니다"),
    NOT_REQUESTED_REFUND(false,3031,"환불 요청 상태가 아닙니다"),
//    FAILED_TO_GET_USER(false, 3012, "회원 정보 조회에 실패하였습니다."),
//    FAILED_TO_POST_USER(false, 3013, "회원가입에 실패하였습니다."),
//    FAILED_TO_LOGIN(false, 3014, "로그인에 실패하였습니다."),
//    FAILED_TO_DELETE_USER(false, 3015, "회원 탈퇴에 실패하였습니다."),
//    FAILED_TO_PATCH_USER(false, 3016, "개인정보 수정에 실패하였습니다."),
//    FAILED_TO_PATCH_PASSWORD(false, 3020, "비밀번호 수정에 실패하였습니다."),

    // 4000 : Database 오류
    UNATHORIZED(false, 401, "인증에 실패하였습니다."),
    FORBIDDEN(false, 403, "권한이 없는 유저입니다."),
    SERVER_ERROR(false, 4000, "서버와의 통신에 실패하였습니다."),
    DATABASE_ERROR(false, 4001, "데이터베이스 연결에 실패하였습니다.");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
