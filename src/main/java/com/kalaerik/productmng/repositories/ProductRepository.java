package com.kalaerik.productmng.repositories;

import com.kalaerik.productmng.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
