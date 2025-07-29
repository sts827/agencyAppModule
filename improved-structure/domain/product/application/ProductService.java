package kr.co.wayplus.travel.domain.product.application;

import kr.co.wayplus.travel.domain.product.domain.Product;
import kr.co.wayplus.travel.domain.product.domain.ProductRepository;
import kr.co.wayplus.travel.domain.product.domain.ProductStatus;
import kr.co.wayplus.travel.domain.product.presentation.dto.ProductCreateRequest;
import kr.co.wayplus.travel.domain.product.presentation.dto.ProductResponse;
import kr.co.wayplus.travel.domain.product.presentation.dto.ProductSearchCondition;
import kr.co.wayplus.travel.domain.product.presentation.dto.ProductUpdateRequest;
import kr.co.wayplus.travel.shared.exception.domain.BusinessException;
import kr.co.wayplus.travel.shared.exception.domain.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    
    private final ProductRepository productRepository;
    private final ProductImageService productImageService;
    private final ProductValidationService productValidationService;
    
    /**
     * 상품 생성
     */
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request, String createdBy) {
        log.info("Creating product: {}", request.getTitle());
        
        // 비즈니스 규칙 검증
        productValidationService.validateProductCreation(request);
        
        Product product = Product.builder()
            .title(request.getTitle())
            .subtitle(request.getSubtitle())
            .description(request.getDescription())
            .basePrice(request.getBasePrice())
            .category(request.getCategory())
            .tourStartDate(request.getTourStartDate())
            .tourEndDate(request.getTourEndDate())
            .reservationStartDate(request.getReservationStartDate())
            .reservationEndDate(request.getReservationEndDate())
            .maxCapacity(request.getMaxCapacity())
            .createdBy(createdBy)
            .build();
        
        Product savedProduct = productRepository.save(product);
        
        // 이미지 처리
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            productImageService.saveProductImages(savedProduct.getId(), request.getImages());
        }
        
        log.info("Product created successfully: {}", savedProduct.getId());
        return ProductResponse.from(savedProduct);
    }
    
    /**
     * 상품 수정
     */
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductUpdateRequest request, String updatedBy) {
        log.info("Updating product: {}", productId);
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, 
                "상품을 찾을 수 없습니다. ID: " + productId));
        
        // 비즈니스 규칙 검증
        productValidationService.validateProductUpdate(product, request);
        
        product.updateProductInfo(
            request.getTitle(),
            request.getSubtitle(),
            request.getDescription(),
            request.getBasePrice(),
            updatedBy
        );
        
        // 이미지 업데이트
        if (request.getImages() != null) {
            productImageService.updateProductImages(productId, request.getImages());
        }
        
        log.info("Product updated successfully: {}", productId);
        return ProductResponse.from(product);
    }
    
    /**
     * 상품 활성화
     */
    @Transactional
    public void activateProduct(Long productId) {
        log.info("Activating product: {}", productId);
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, 
                "상품을 찾을 수 없습니다. ID: " + productId));
        
        productValidationService.validateProductActivation(product);
        product.activate();
        
        log.info("Product activated successfully: {}", productId);
    }
    
    /**
     * 상품 비활성화
     */
    @Transactional
    public void deactivateProduct(Long productId) {
        log.info("Deactivating product: {}", productId);
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, 
                "상품을 찾을 수 없습니다. ID: " + productId));
        
        product.deactivate();
        
        log.info("Product deactivated successfully: {}", productId);
    }
    
    /**
     * 상품 삭제
     */
    @Transactional
    public void deleteProduct(Long productId) {
        log.info("Deleting product: {}", productId);
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, 
                "상품을 찾을 수 없습니다. ID: " + productId));
        
        product.delete();
        
        log.info("Product deleted successfully: {}", productId);
    }
    
    /**
     * 상품 단건 조회
     */
    public ProductResponse getProduct(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, 
                "상품을 찾을 수 없습니다. ID: " + productId));
        
        return ProductResponse.from(product);
    }
    
    /**
     * 상품 목록 조회 (관리자용)
     */
    public Page<ProductResponse> getProductsForAdmin(ProductSearchCondition condition, Pageable pageable) {
        return productRepository.findAllWithCondition(condition, pageable)
            .map(ProductResponse::from);
    }
    
    /**
     * 활성 상품 목록 조회 (고객용)
     */
    public Page<ProductResponse> getActiveProducts(ProductSearchCondition condition, Pageable pageable) {
        condition.setStatus(ProductStatus.ACTIVE);
        return productRepository.findAllWithCondition(condition, pageable)
            .map(ProductResponse::from);
    }
    
    /**
     * 예약 가능한 상품 목록 조회
     */
    public Page<ProductResponse> getAvailableProducts(ProductSearchCondition condition, Pageable pageable) {
        return productRepository.findAvailableProducts(condition, pageable)
            .map(ProductResponse::from);
    }
    
    /**
     * 상품 예약 인원 증가
     */
    @Transactional
    public void increaseReservation(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, 
                "상품을 찾을 수 없습니다. ID: " + productId));
        
        product.increaseReservation();
        
        log.info("Product reservation increased: {} (current: {})", 
            productId, product.getCurrentReservations());
    }
    
    /**
     * 상품 예약 인원 감소
     */
    @Transactional
    public void decreaseReservation(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, 
                "상품을 찾을 수 없습니다. ID: " + productId));
        
        product.decreaseReservation();
        
        log.info("Product reservation decreased: {} (current: {})", 
            productId, product.getCurrentReservations());
    }
}