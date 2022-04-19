package com.kalaerik.productmng.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalaerik.productmng.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProducts() throws JsonProcessingException;
    Product addProduct(Product product) throws Exception;
    Product getProduct(Integer id) throws JsonProcessingException;
    Product updateProduct(Integer id, Product product);
    void deleteProduct(Integer id);

}
