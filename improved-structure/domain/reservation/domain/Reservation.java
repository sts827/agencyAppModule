package kr.co.wayplus.travel.domain.reservation.domain;

import kr.co.wayplus.travel.shared.exception.domain.BusinessException;
import kr.co.wayplus.travel.shared.exception.domain.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {
    
    private Long id;
    private String reservationNumber;
    private Long productId;
    private Long userId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private Integer participantCount;
    private BigDecimal totalAmount;
    private ReservationStatus status;
    private LocalDateTime reservationDate;
    private LocalDateTime tourDate;
    private String specialRequests;
    private List<ReservationParticipant> participants;
    private PaymentInfo paymentInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    @Builder
    public Reservation(String reservationNumber, Long productId, Long userId,
                      String customerName, String customerEmail, String customerPhone,
                      Integer participantCount, BigDecimal totalAmount,
                      LocalDateTime tourDate, String specialRequests,
                      List<ReservationParticipant> participants, String createdBy) {
        validateReservationData(customerName, customerEmail, customerPhone, 
                               participantCount, totalAmount, tourDate);
        
        this.reservationNumber = reservationNumber;
        this.productId = productId;
        this.userId = userId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.participantCount = participantCount;
        this.totalAmount = totalAmount;
        this.tourDate = tourDate;
        this.specialRequests = specialRequests;
        this.participants = participants;
        this.status = ReservationStatus.PENDING;
        this.reservationDate = LocalDateTime.now();
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // 비즈니스 로직 메서드들
    public void confirm() {
        if (this.status != ReservationStatus.PENDING) {
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATUS, 
                "대기 중인 예약만 확정할 수 있습니다.");
        }
        this.status = ReservationStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void cancel(String reason) {
        if (this.status == ReservationStatus.CANCELLED || 
            this.status == ReservationStatus.COMPLETED) {
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATUS, 
                "취소하거나 완료된 예약은 취소할 수 없습니다.");
        }
        
        // 여행 시작 24시간 전까지만 취소 가능
        if (LocalDateTime.now().plusHours(24).isAfter(this.tourDate)) {
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_DATE, 
                "여행 시작 24시간 전까지만 취소 가능합니다.");
        }
        
        this.status = ReservationStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void complete() {
        if (this.status != ReservationStatus.CONFIRMED) {
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATUS, 
                "확정된 예약만 완료 처리할 수 있습니다.");
        }
        
        if (LocalDateTime.now().isBefore(this.tourDate)) {
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_DATE, 
                "여행 시작일 이후에만 완료 처리할 수 있습니다.");
        }
        
        this.status = ReservationStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updatePaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateCustomerInfo(String customerName, String customerEmail, 
                                  String customerPhone, String updatedBy) {
        validateCustomerInfo(customerName, customerEmail, customerPhone);
        
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateParticipants(List<ReservationParticipant> participants) {
        if (participants == null || participants.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, 
                "참가자 정보는 필수입니다.");
        }
        
        if (participants.size() != this.participantCount) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, 
                "참가자 수가 일치하지 않습니다.");
        }
        
        this.participants = participants;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean canCancel() {
        return (this.status == ReservationStatus.PENDING || 
                this.status == ReservationStatus.CONFIRMED) &&
               LocalDateTime.now().plusHours(24).isBefore(this.tourDate);
    }
    
    public boolean canModify() {
        return this.status == ReservationStatus.PENDING &&
               LocalDateTime.now().plusHours(48).isBefore(this.tourDate);
    }
    
    public boolean isPaymentRequired() {
        return this.paymentInfo == null || 
               this.paymentInfo.getPaymentStatus() != PaymentStatus.COMPLETED;
    }
    
    private void validateReservationData(String customerName, String customerEmail, 
                                       String customerPhone, Integer participantCount, 
                                       BigDecimal totalAmount, LocalDateTime tourDate) {
        validateCustomerInfo(customerName, customerEmail, customerPhone);
        
        if (participantCount == null || participantCount <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, 
                "참가자 수는 1명 이상이어야 합니다.");
        }
        
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, 
                "총 금액은 0보다 커야 합니다.");
        }
        
        if (tourDate == null || tourDate.isBefore(LocalDateTime.now().plusHours(24))) {
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_DATE, 
                "여행 날짜는 현재 시점으로부터 24시간 이후여야 합니다.");
        }
    }
    
    private void validateCustomerInfo(String customerName, String customerEmail, String customerPhone) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "고객명은 필수입니다.");
        }
        
        if (customerEmail == null || customerEmail.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이메일은 필수입니다.");
        }
        
        if (customerPhone == null || customerPhone.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "연락처는 필수입니다.");
        }
    }
}