package com.kalaerik.productmng.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalaerik.productmng.model.Product;
import com.kalaerik.productmng.repositories.ProductRepository;
import com.kalaerik.productmng.services.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    ProductServiceImpl productService;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;


    @BeforeEach
    public void setUp(){
    System.out.println("u SetUp-u sam");
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        System.out.println("this.mockMvc: " + this.mockMvc);
    }

    @Test
    public void When_FetchAllRecords_Expect_ListWithPriceInEuro() throws Exception {
        Product PRODUCT_1 = new Product(1, "1234567890", "Najbolja Pričam", 123.45, 12.35, "Najbolja Pričam - desc", "true");
        Product PRODUCT_2 = new Product(2, "0987654321", "Najbolja Start", 89.45, 8.95, "Najbolja Start - desc", "false");
        Product PRODUCT_3 = new Product(3, "1029384756", "Najbolja Mala", 23.45, 2.35, "Najbolja Mala - desc", "true");

        List<Product> givenArray = new ArrayList<>(Arrays.asList(PRODUCT_1, PRODUCT_2, PRODUCT_3));

        Mockito.when(productService.getProducts()).thenReturn(givenArray);

        mockMvc.perform(MockMvcRequestBuilders
                 .get("/api/v1/products")
                 .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(PRODUCT_1.getId())))
                .andExpect(jsonPath("$[0].code", is(PRODUCT_1.getCode())))
                .andExpect(jsonPath("$[0].price_hrk", is(PRODUCT_1.getPrice_hrk())))
                .andExpect(jsonPath("$[0].price_eur", is(PRODUCT_1.getPrice_eur())))
                .andExpect(jsonPath("$[0].description", is(PRODUCT_1.getDescription())))
                .andExpect(jsonPath("$[0].is_available", is(PRODUCT_1.getIs_available())))
                .andExpect(jsonPath("$[1].id", is(PRODUCT_2.getId())))
                .andExpect(jsonPath("$[1].code", is(PRODUCT_2.getCode())))
                .andExpect(jsonPath("$[1].price_hrk", is(PRODUCT_2.getPrice_hrk())))
                .andExpect(jsonPath("$[1].price_eur", is(PRODUCT_2.getPrice_eur())))
                .andExpect(jsonPath("$[1].description", is(PRODUCT_2.getDescription())))
                .andExpect(jsonPath("$[1].is_available", is(PRODUCT_2.getIs_available())))
                .andExpect(jsonPath("$[2].id", is(PRODUCT_3.getId())))
                .andExpect(jsonPath("$[2].code", is(PRODUCT_3.getCode())))
                .andExpect(jsonPath("$[2].price_hrk", is(PRODUCT_3.getPrice_hrk())))
                .andExpect(jsonPath("$[2].price_eur", is(PRODUCT_3.getPrice_eur())))
                .andExpect(jsonPath("$[2].description", is(PRODUCT_3.getDescription())))
                .andExpect(jsonPath("$[2].is_available", is(PRODUCT_3.getIs_available())));

        verify(productService).getProducts();
    }

    @Test
    public void When_FetchOneRecord_Expect_OneProductWithPriceInEuro() throws Exception{
        Product PRODUCT_1 = new Product(1, "1234567890", "Najbolja Pričam", 123.45, 12.35, "Najbolja Pričam - desc", "true");

        Mockito.when(productService.getProduct(PRODUCT_1.getId())).thenReturn(PRODUCT_1);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/products/"+ PRODUCT_1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.id", is(PRODUCT_1.getId())))
                .andExpect(jsonPath("$.code", is(PRODUCT_1.getCode())))
                .andExpect(jsonPath("$.price_hrk", is(PRODUCT_1.getPrice_hrk())))
                .andExpect(jsonPath("$.price_eur", is(PRODUCT_1.getPrice_eur())))
                .andExpect(jsonPath("$.description", is(PRODUCT_1.getDescription())))
                .andExpect(jsonPath("$.is_available", is(PRODUCT_1.getIs_available())));

        verify(productService).getProduct(PRODUCT_1.getId());
    }

    @Test
    public void When_PostProduct_Expect_SameResponse() throws Exception {
        Product PRODUCT_1 = new Product(1, "1234567890", "Najbolja Pričam", 123.45, 0, "Najbolja Pričam - desc", "true");

        Mockito.when(productService.addProduct(PRODUCT_1)).thenReturn(PRODUCT_1);

        String json = new ObjectMapper().writeValueAsString(PRODUCT_1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(mockRequest)
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.id", is(PRODUCT_1.getId())))
                .andExpect(jsonPath("$.code", is(PRODUCT_1.getCode())))
                .andExpect(jsonPath("$.price_hrk", is(PRODUCT_1.getPrice_hrk())))
                .andExpect(jsonPath("$.price_eur", is(PRODUCT_1.getPrice_eur())))
                .andExpect(jsonPath("$.description", is(PRODUCT_1.getDescription())))
                .andExpect(jsonPath("$.is_available", is(PRODUCT_1.getIs_available())));

        verify(productService).addProduct(PRODUCT_1);
    }

    @Test
    public void When_PutProduct_Expect_UpdatedResponse() throws Exception {
        Product PRODUCT_1 = new Product(1, "1234567890", "Najbolja Pričam", 123.45, 0, "Najbolja Pričam - desc", "true");

        Product updatedProduct = new Product(1 ,"11111111111", "Najbolja UPDATED", 200.20, 0, "Najbolja UPDATED - desc", "false");

        Mockito.when(productService.updateProduct(PRODUCT_1.getId(),updatedProduct)).thenReturn(updatedProduct);

        String json = new ObjectMapper().writeValueAsString(updatedProduct);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/v1/products/" + PRODUCT_1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(mockRequest)
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.id", is(updatedProduct.getId())))
                .andExpect(jsonPath("$.code", is(updatedProduct.getCode())))
                .andExpect(jsonPath("$.price_hrk", is(updatedProduct.getPrice_hrk())))
                .andExpect(jsonPath("$.price_eur", is(updatedProduct.getPrice_eur())))
                .andExpect(jsonPath("$.description", is(updatedProduct.getDescription())))
                .andExpect(jsonPath("$.is_available", is(updatedProduct.getIs_available())));

        verify(productService).updateProduct(PRODUCT_1.getId(),updatedProduct);

    }

    @Test
    public void When_DeleteProduct_Expect_EmptyListResponse() throws Exception {
        Product PRODUCT_1 = new Product(1, "1234567890", "Najbolja Pričam", 123.45, 0, "Najbolja Pričam - desc", "true");

        Mockito.when(productService.addProduct(PRODUCT_1)).thenReturn(PRODUCT_1);

        String json = new ObjectMapper().writeValueAsString(PRODUCT_1);

        MockHttpServletRequestBuilder mockRequestPost = MockMvcRequestBuilders.post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(mockRequestPost)
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.id", is(PRODUCT_1.getId())));


        MockHttpServletRequestBuilder mockRequestDel = MockMvcRequestBuilders.delete("/api/v1/products/" + PRODUCT_1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequestDel)
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").doesNotExist());

        verify(productService).deleteProduct(PRODUCT_1.getId());

    }

    }