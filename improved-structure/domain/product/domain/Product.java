package kr.co.wayplus.travel.domain.product.domain;

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
public class Product {
    
    private Long id;
    private String title;
    private String subtitle;
    private String description;
    private BigDecimal basePrice;
    private ProductStatus status;
    private ProductCategory category;
    private LocalDateTime tourStartDate;
    private LocalDateTime tourEndDate;
    private LocalDateTime reservationStartDate;
    private LocalDateTime reservationEndDate;
    private Integer maxCapacity;
    private Integer currentReservations;
    private String thumbnailImage;
    private List<ProductImage> images;
    private List<PriceOption> priceOptions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    @Builder
    public Product(String title, String subtitle, String description, 
                  BigDecimal basePrice, ProductCategory category,
                  LocalDateTime tourStartDate, LocalDateTime tourEndDate,
                  LocalDateTime reservationStartDate, LocalDateTime reservationEndDate,
                  Integer maxCapacity, String createdBy) {
        validateProductData(title, basePrice, tourStartDate, tourEndDate, 
                          reservationStartDate, reservationEndDate, maxCapacity);
        
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.basePrice = basePrice;
        this.category = category;
        this.tourStartDate = tourStartDate;
        this.tourEndDate = tourEndDate;
        this.reservationStartDate = reservationStartDate;
        this.reservationEndDate = reservationEndDate;
        this.maxCapacity = maxCapacity;
        this.currentReservations = 0;
        this.status = ProductStatus.DRAFT;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // 비즈니스 로직 메서드들
    public void activate() {
        if (this.status == ProductStatus.DELETED) {
            throw new BusinessException(ErrorCode.INVALID_PRODUCT_STATUS, 
                "삭제된 상품은 활성화할 수 없습니다.");
        }
        this.status = ProductStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void delete() {
        if (this.currentReservations > 0) {
            throw new BusinessException(ErrorCode.INVALID_PRODUCT_STATUS, 
                "예약이 있는 상품은 삭제할 수 없습니다.");
        }
        this.status = ProductStatus.DELETED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isReservationAvailable() {
        LocalDateTime now = LocalDateTime.now();
        return this.status == ProductStatus.ACTIVE &&
               now.isAfter(reservationStartDate) &&
               now.isBefore(reservationEndDate) &&
               hasAvailableCapacity();
    }
    
    public boolean hasAvailableCapacity() {
        return currentReservations < maxCapacity;
    }
    
    public void increaseReservation() {
        if (!hasAvailableCapacity()) {
            throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK, 
                "상품 예약 가능 인원이 초과되었습니다.");
        }
        this.currentReservations++;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void decreaseReservation() {
        if (this.currentReservations <= 0) {
            throw new BusinessException(ErrorCode.INVALID_PRODUCT_STATUS, 
                "예약 인원을 감소시킬 수 없습니다.");
        }
        this.currentReservations--;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateProductInfo(String title, String subtitle, String description,
                                 BigDecimal basePrice, String updatedBy) {
        validateProductData(title, basePrice, this.tourStartDate, this.tourEndDate,
                          this.reservationStartDate, this.reservationEndDate, this.maxCapacity);
        
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.basePrice = basePrice;
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }
    
    public BigDecimal calculatePrice(PriceOption option) {
        if (option == null) {
            return this.basePrice;
        }
        return this.basePrice.add(option.getAdditionalPrice());
    }
    
    private void validateProductData(String title, BigDecimal basePrice,
                                   LocalDateTime tourStartDate, LocalDateTime tourEndDate,
                                   LocalDateTime reservationStartDate, LocalDateTime reservationEndDate,
                                   Integer maxCapacity) {
        if (title == null || title.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "상품명은 필수입니다.");
        }
        
        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCode.INVALID_PRICE_RANGE, "가격은 0 이상이어야 합니다.");
        }
        
        if (tourStartDate != null && tourEndDate != null && tourStartDate.isAfter(tourEndDate)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, 
                "여행 시작일은 종료일보다 빠를 수 없습니다.");
        }
        
        if (reservationStartDate != null && reservationEndDate != null && 
            reservationStartDate.isAfter(reservationEndDate)) {
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_DATE, 
                "예약 시작일은 종료일보다 빠를 수 없습니다.");
        }
        
        if (maxCapacity != null && maxCapacity <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, 
                "최대 인원은 1명 이상이어야 합니다.");
        }
    }
}