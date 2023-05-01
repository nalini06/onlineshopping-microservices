package com.onlineshopping.product.service.repository;

import com.onlineshopping.product.service.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {

}
