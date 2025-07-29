package kr.co.wayplus.travel.shared.exception.domain;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    
    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C002", "엔티티를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 내부 오류입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C004", "잘못된 타입입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "C005", "접근이 거부되었습니다."),
    
    // User Domain
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "U002", "비밀번호가 일치하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "U003", "이미 존재하는 이메일입니다."),
    ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "U004", "계정이 잠겨있습니다."),
    ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "U005", "비활성화된 계정입니다."),
    
    // Product Domain
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "상품을 찾을 수 없습니다."),
    INVALID_PRODUCT_STATUS(HttpStatus.BAD_REQUEST, "P002", "잘못된 상품 상태입니다."),
    PRODUCT_OUT_OF_STOCK(HttpStatus.CONFLICT, "P003", "상품 재고가 부족합니다."),
    INVALID_PRICE_RANGE(HttpStatus.BAD_REQUEST, "P004", "잘못된 가격 범위입니다."),
    
    // Reservation Domain
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "예약을 찾을 수 없습니다."),
    INVALID_RESERVATION_DATE(HttpStatus.BAD_REQUEST, "R002", "잘못된 예약 날짜입니다."),
    RESERVATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "R003", "이미 예약이 존재합니다."),
    RESERVATION_CANCELLED(HttpStatus.BAD_REQUEST, "R004", "취소된 예약입니다."),
    
    // Payment Domain
    PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "PAY001", "결제에 실패했습니다."),
    INVALID_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "PAY002", "잘못된 결제 금액입니다."),
    
    // File Domain
    FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "F001", "파일 업로드에 실패했습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "F002", "지원하지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "F003", "파일 크기가 초과되었습니다.");
    
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    
    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}