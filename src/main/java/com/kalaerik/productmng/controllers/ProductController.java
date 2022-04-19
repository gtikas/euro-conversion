package com.kalaerik.productmng.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalaerik.productmng.model.Product;
import com.kalaerik.productmng.services.ProductService;
import com.kalaerik.productmng.services.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getProducts() throws JsonProcessingException {
        return productService.getProducts();
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) throws Exception {
        System.out.println("prije productService.addProduct(product)" );
        Product savedProduct = productService.addProduct(product);
        System.out.println("nakon  productService.addProduct(product)");
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @RequestMapping(method=RequestMethod.GET, value = "/{id}")
    public Product getProduct(@PathVariable Integer id) throws JsonProcessingException {
        return productService.getProduct(id);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public Product updateProduct(@PathVariable Integer id, @RequestBody Product product){
        return productService.updateProduct(id, product);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteProduct(@PathVariable Integer id){
        productService.deleteProduct(id);
    }

}
