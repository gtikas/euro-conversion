package com.kalaerik.productmng.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalaerik.productmng.model.Product;
import com.kalaerik.productmng.model.Rate;
import com.kalaerik.productmng.repositories.ProductRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
//@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    /*
       private static final int PORT = 443;
        private static final String HOST = "api.hnb.hr";

        private static WireMockServer server = new WireMockServer(PORT);

        @Before
        public static void setUp(){

            server.start();
            ResponseDefinitionBuilder mockResponse = new ResponseDefinitionBuilder();
            mockResponse.withStatus(200).withBody("[\n" +
                    "{\n" +
                    "\"Broj tečajnice\": \"71\",\n" +
                    "\"Datum primjene\": \"12.04.2022\",\n" +
                    "\"Država\": \"EMU\",\n" +
                    "\"Šifra valute\": \"978\",\n" +
                    "\"Valuta\": \"EUR\",\n" +
                    "\"Jedinica\": 1,\n" +
                    "\"Kupovni za devize\": \"7,530933\",\n" +
                    "\"Srednji za devize\": \"7,553594\",\n" +
                    "\"Prodajni za devize\": \"7,576255\"\n" +
                    "}\n" +
                    "]");

            WireMock.configureFor(HOST, PORT);
            WireMock.stubFor(
                    WireMock.get(urlPathEqualTo("/tecajn/v1?valuta=EUR")).willReturn(mockResponse)
            );
            System.out.println("Done with wiremock");
        }
    */
    @Mock
    private CurrencyClient currencyClient;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    Rate RATE_1 = new Rate("EUR","10","10","10");

    @Test
    public void When_FetchingSingleProductById_Expect_ResultWithCorrectPricesInEuro() throws JsonProcessingException {
        Product PRODUCT_1 = new Product(1, "2029384756", "Najbolja Ultra", 53.45, 0, "Najbolja Ultra - desc", "true");

        Mockito.when(productRepository.findById(PRODUCT_1.getId())).thenReturn(Optional.of(PRODUCT_1));
        Mockito.when(currencyClient.getHrkEurRate()).thenReturn(Double.valueOf(RATE_1.getMiddleRate()));

        Product product = productServiceImpl.getProduct(PRODUCT_1.getId());

        assertThat(product.getId()).isEqualTo(PRODUCT_1.getId());
        assertThat(product.getCode()).isSameAs(PRODUCT_1.getCode());
        assertThat(product.getName()).isSameAs(PRODUCT_1.getName());
        assertThat(product.getPrice_hrk()).isEqualTo(PRODUCT_1.getPrice_hrk());
        assertThat(product.getPrice_eur()).isEqualTo(5.35);
        assertThat(product.getDescription()).isSameAs(PRODUCT_1.getDescription());
        assertThat(product.getIs_available()).isSameAs(PRODUCT_1.getIs_available());

        verify(productRepository).findById(PRODUCT_1.getId());

    }

    @Test
    public void When_FetchingAllProducts_Expect_ResultsWithCorrectPricesInEuro() throws JsonProcessingException {

        Product PRODUCT_2 = new Product(1, "1234567890", "Najbolja Pričam", 123.45, 0, "Najbolja Pričam - desc", "true");
        Product PRODUCT_3 = new Product(2, "0987654321", "Najbolja Start", 89.45, 0, "Najbolja Start - desc", "false");
        Product PRODUCT_4 = new Product(3, "1029384756", "Najbolja Mala", 23.43, 0, "Najbolja Mala - desc", "true");

        List<Product> givenArray = new ArrayList<>(Arrays.asList(PRODUCT_2, PRODUCT_3, PRODUCT_4));
        Mockito.when(productRepository.findAll()).thenReturn(givenArray);
        Mockito.when(currencyClient.getHrkEurRate()).thenReturn(Double.valueOf(RATE_1.getMiddleRate()));
        List<Product> products = productServiceImpl.getProducts();

        assertThat(products.get(0).getId()).isEqualTo(PRODUCT_2.getId());
        assertThat(products.get(0).getCode()).isSameAs(PRODUCT_2.getCode());
        assertThat(products.get(0).getName()).isSameAs(PRODUCT_2.getName());
        assertThat(products.get(0).getPrice_hrk()).isEqualTo(PRODUCT_2.getPrice_hrk());
        assertThat(products.get(0).getPrice_eur()).isEqualTo(12.35);
        assertThat(products.get(0).getDescription()).isSameAs(PRODUCT_2.getDescription());
        assertThat(products.get(0).getIs_available()).isSameAs(PRODUCT_2.getIs_available());

        assertThat(products.get(1).getId()).isEqualTo(PRODUCT_3.getId());
        assertThat(products.get(1).getCode()).isSameAs(PRODUCT_3.getCode());
        assertThat(products.get(1).getName()).isSameAs(PRODUCT_3.getName());
        assertThat(products.get(1).getPrice_hrk()).isEqualTo(PRODUCT_3.getPrice_hrk());
        assertThat(products.get(1).getPrice_eur()).isEqualTo(8.95);
        assertThat(products.get(1).getDescription()).isSameAs(PRODUCT_3.getDescription());
        assertThat(products.get(1).getIs_available()).isSameAs(PRODUCT_3.getIs_available());

        assertThat(products.get(2).getId()).isEqualTo(PRODUCT_4.getId());
        assertThat(products.get(2).getCode()).isSameAs(PRODUCT_4.getCode());
        assertThat(products.get(2).getName()).isSameAs(PRODUCT_4.getName());
        assertThat(products.get(2).getPrice_hrk()).isEqualTo(PRODUCT_4.getPrice_hrk());
        assertThat(products.get(2).getPrice_eur()).isEqualTo(2.34);
        assertThat(products.get(2).getDescription()).isSameAs(PRODUCT_4.getDescription());
        assertThat(products.get(2).getIs_available()).isSameAs(PRODUCT_4.getIs_available());

        verify(productRepository).findAll();
    }

    @Test
    public void When_CreateProduct_Expect_SameResponseBack() throws Exception {
        Product PRODUCT_1 = new Product(1, "1234567890", "Najbolja Pričam", 123.45, 0, "Najbolja Pričam - desc", "true");

        Mockito.when(productRepository.save(PRODUCT_1)).thenReturn(PRODUCT_1);

        Product product = productServiceImpl.addProduct(PRODUCT_1);

        assertThat(product.getId()).isEqualTo(PRODUCT_1.getId());
        assertThat(product.getCode()).isSameAs(PRODUCT_1.getCode());
        assertThat(product.getName()).isSameAs(PRODUCT_1.getName());
        assertThat(product.getPrice_hrk()).isEqualTo(PRODUCT_1.getPrice_hrk());
        assertThat(product.getPrice_eur()).isEqualTo(0);
        assertThat(product.getDescription()).isSameAs(PRODUCT_1.getDescription());
        assertThat(product.getIs_available()).isSameAs(PRODUCT_1.getIs_available());

    }

    @Test
    public void When_UpdateProduct_Expect_FindExistingOneByIdAndSave(){
        Product PRODUCT_1 = new Product(1, "1234567890", "Najbolja Pričam", 123.45, 0, "Najbolja Pričam - desc", "true");

        Product updatedProduct = new Product(1 ,"11111111111", "Najbolja UPDATED", 200.20, 0, "Najbolja UPDATED - desc", "false");

        Mockito.when(productRepository.findById(updatedProduct.getId())).thenReturn(Optional.of(PRODUCT_1));
        Mockito.when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);

        Product product = productServiceImpl.updateProduct(updatedProduct.getId(),updatedProduct);

        assertThat(product.getId()).isEqualTo(updatedProduct.getId());
        assertThat(product.getCode()).isSameAs(updatedProduct.getCode());
        assertThat(product.getName()).isSameAs(updatedProduct.getName());
        assertThat(product.getPrice_hrk()).isEqualTo(updatedProduct.getPrice_hrk());
        assertThat(product.getPrice_eur()).isEqualTo(0);
        assertThat(product.getDescription()).isSameAs(updatedProduct.getDescription());
        assertThat(product.getIs_available()).isSameAs(updatedProduct.getIs_available());

        verify(productRepository).findById(PRODUCT_1.getId());

    }

}
