package kr.co.wayplus.travel.domain.product.presentation;

import kr.co.wayplus.travel.domain.product.application.ProductService;
import kr.co.wayplus.travel.domain.product.presentation.dto.*;
import kr.co.wayplus.travel.shared.common.ApiResponse;
import kr.co.wayplus.travel.shared.common.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {
    
    private final ProductService productService;
    
    /**
     * 상품 생성 (관리자 전용)
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("Creating product by user: {}", userDetails.getUsername());
        
        ProductResponse response = productService.createProduct(request, userDetails.getUsername());
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("상품이 성공적으로 생성되었습니다.", response));
    }
    
    /**
     * 상품 수정 (관리자 전용)
     */
    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable @Positive Long productId,
            @Valid @RequestBody ProductUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("Updating product {} by user: {}", productId, userDetails.getUsername());
        
        ProductResponse response = productService.updateProduct(productId, request, userDetails.getUsername());
        
        return ResponseEntity.ok(ApiResponse.success("상품이 성공적으로 수정되었습니다.", response));
    }
    
    /**
     * 상품 활성화 (관리자 전용)
     */
    @PatchMapping("/{productId}/activate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> activateProduct(@PathVariable @Positive Long productId) {
        
        log.info("Activating product: {}", productId);
        
        productService.activateProduct(productId);
        
        return ResponseEntity.ok(ApiResponse.success("상품이 활성화되었습니다."));
    }
    
    /**
     * 상품 비활성화 (관리자 전용)
     */
    @PatchMapping("/{productId}/deactivate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivateProduct(@PathVariable @Positive Long productId) {
        
        log.info("Deactivating product: {}", productId);
        
        productService.deactivateProduct(productId);
        
        return ResponseEntity.ok(ApiResponse.success("상품이 비활성화되었습니다."));
    }
    
    /**
     * 상품 삭제 (관리자 전용)
     */
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable @Positive Long productId) {
        
        log.info("Deleting product: {}", productId);
        
        productService.deleteProduct(productId);
        
        return ResponseEntity.ok(ApiResponse.success("상품이 삭제되었습니다."));
    }
    
    /**
     * 상품 단건 조회
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable @Positive Long productId) {
        
        ProductResponse response = productService.getProduct(productId);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * 상품 목록 조회 (관리자용 - 모든 상태)
     */
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProductsForAdmin(
            @ModelAttribute ProductSearchCondition condition,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<ProductResponse> products = productService.getProductsForAdmin(condition, pageable);
        PageResponse<ProductResponse> response = PageResponse.of(products);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * 활성 상품 목록 조회 (고객용)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getActiveProducts(
            @ModelAttribute ProductSearchCondition condition,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<ProductResponse> products = productService.getActiveProducts(condition, pageable);
        PageResponse<ProductResponse> response = PageResponse.of(products);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * 예약 가능한 상품 목록 조회
     */
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAvailableProducts(
            @ModelAttribute ProductSearchCondition condition,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<ProductResponse> products = productService.getAvailableProducts(condition, pageable);
        PageResponse<ProductResponse> response = PageResponse.of(products);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * 상품 검색 (키워드 검색)
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> searchProducts(
            @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        
        ProductSearchCondition condition = ProductSearchCondition.builder()
            .keyword(keyword)
            .build();
            
        Page<ProductResponse> products = productService.getActiveProducts(condition, pageable);
        PageResponse<ProductResponse> response = PageResponse.of(products);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}