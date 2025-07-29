package kr.co.wayplus.travel.domain.product.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.wayplus.travel.domain.product.domain.ProductCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {
    
    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 200, message = "상품명은 200자를 초과할 수 없습니다.")
    private String title;
    
    @Size(max = 300, message = "부제목은 300자를 초과할 수 없습니다.")
    private String subtitle;
    
    @NotBlank(message = "상품 설명은 필수입니다.")
    @Size(max = 5000, message = "상품 설명은 5000자를 초과할 수 없습니다.")
    private String description;
    
    @NotNull(message = "기본 가격은 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = false, message = "가격은 0보다 커야 합니다.")
    @Digits(integer = 10, fraction = 2, message = "가격 형식이 올바르지 않습니다.")
    private BigDecimal basePrice;
    
    @NotNull(message = "상품 카테고리는 필수입니다.")
    private ProductCategory category;
    
    @NotNull(message = "여행 시작일은 필수입니다.")
    @Future(message = "여행 시작일은 현재 시점 이후여야 합니다.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime tourStartDate;
    
    @NotNull(message = "여행 종료일은 필수입니다.")
    @Future(message = "여행 종료일은 현재 시점 이후여야 합니다.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime tourEndDate;
    
    @NotNull(message = "예약 시작일은 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservationStartDate;
    
    @NotNull(message = "예약 종료일은 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservationEndDate;
    
    @NotNull(message = "최대 인원은 필수입니다.")
    @Min(value = 1, message = "최대 인원은 1명 이상이어야 합니다.")
    @Max(value = 100, message = "최대 인원은 100명을 초과할 수 없습니다.")
    private Integer maxCapacity;
    
    @Size(max = 1000, message = "포함 사항은 1000자를 초과할 수 없습니다.")
    private String includeItems;
    
    @Size(max = 1000, message = "불포함 사항은 1000자를 초과할 수 없습니다.")
    private String excludeItems;
    
    @Size(max = 1000, message = "준비물은 1000자를 초과할 수 없습니다.")
    private String requiredItems;
    
    @Size(max = 2000, message = "주의사항은 2000자를 초과할 수 없습니다.")
    private String notice;
    
    private List<MultipartFile> images;
    
    private List<PriceOptionRequest> priceOptions;
    
    @Builder
    public ProductCreateRequest(String title, String subtitle, String description,
                               BigDecimal basePrice, ProductCategory category,
                               LocalDateTime tourStartDate, LocalDateTime tourEndDate,
                               LocalDateTime reservationStartDate, LocalDateTime reservationEndDate,
                               Integer maxCapacity, String includeItems, String excludeItems,
                               String requiredItems, String notice, List<MultipartFile> images,
                               List<PriceOptionRequest> priceOptions) {
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
        this.includeItems = includeItems;
        this.excludeItems = excludeItems;
        this.requiredItems = requiredItems;
        this.notice = notice;
        this.images = images;
        this.priceOptions = priceOptions;
    }
    
    // 커스텀 검증 메서드
    @AssertTrue(message = "여행 종료일은 시작일보다 늦어야 합니다.")
    public boolean isTourDateValid() {
        if (tourStartDate == null || tourEndDate == null) {
            return true; // null 검증은 @NotNull에서 처리
        }
        return tourEndDate.isAfter(tourStartDate);
    }
    
    @AssertTrue(message = "예약 종료일은 시작일보다 늦어야 합니다.")
    public boolean isReservationDateValid() {
        if (reservationStartDate == null || reservationEndDate == null) {
            return true;
        }
        return reservationEndDate.isAfter(reservationStartDate);
    }
    
    @AssertTrue(message = "예약 종료일은 여행 시작일보다 이전이어야 합니다.")
    public boolean isReservationEndBeforeTourStart() {
        if (reservationEndDate == null || tourStartDate == null) {
            return true;
        }
        return reservationEndDate.isBefore(tourStartDate);
    }
}