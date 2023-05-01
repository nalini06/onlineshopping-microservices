package com.onlineshopping.product.service.service;

import com.onlineshopping.product.service.dto.ProductRequest;
import com.onlineshopping.product.service.dto.ProductResponse;
import com.onlineshopping.product.service.model.Product;
import com.onlineshopping.product.service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public Product createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                          .name(productRequest.getName())
                          .description(productRequest.getDescription())
                          .price(productRequest.getPrice())
                          .build();
        Product product1 = productRepository.save(product);
        log.info("Product {} is saved", product.getId());
        return product1;
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> productResponses = productRepository.findAll();
       return productResponses.stream().map(product -> mapToProductResponse(product)).toList();
    }

    public ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .description(product.getDescription())
                .price(product.getPrice())
                .name(product.getName())
                .build();
    }

}
