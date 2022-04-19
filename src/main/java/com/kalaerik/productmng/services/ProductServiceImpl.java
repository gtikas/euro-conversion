package com.kalaerik.productmng.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalaerik.productmng.exceptions.DataNotFound;
import com.kalaerik.productmng.model.Product;
import com.kalaerik.productmng.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

  //  @Value("${conversion.resourceUrl}")
 //   private String url;

    @Autowired
    private ProductRepository productRepository;

  //  @Autowired
  //  private RestTemplate restTemplate;

    @Autowired
    private CurrencyClient currencyClient;

    public List<Product> getProducts() throws JsonProcessingException {
        List<Product> products = new ArrayList<>();
        double hrkEurRate = currencyClient.getHrkEurRate();
        for (Product product : productRepository.findAll()) {
            product.setPrice_eur(convertHrkToEur(product.getPrice_hrk(), hrkEurRate));
            products.add(product);
        }
        return products;
    }

    public Product addProduct(Product product) throws Exception {
        try{
            return productRepository.save(product);
        }catch (Exception e){
            throw new Exception("Something wrong! " );
        }
    }

    public Product getProduct(Integer id) throws JsonProcessingException {
        try{
            Product product = productRepository.findById(id).get();
            product.setPrice_eur(convertHrkToEur(product.getPrice_hrk(), currencyClient.getHrkEurRate()));
            return product;
        } catch (Exception e){
            throw new DataNotFound(e.getMessage());
        }
    }

    public Product updateProduct(Integer id, Product product) {
        try{
            productRepository.findById(id).get();
        } catch (Exception e){
            throw new DataNotFound("Provided id does not exists");
        }
        try{
            product.setId(id);
            return productRepository.save(product);
        } catch (Exception e){
        throw new DataNotFound("Must provide complete request "+e.getMessage());
    }

}

    public void deleteProduct(Integer id) {
        try{
            productRepository.findById(id).get();

        } catch (Exception e){
            throw new DataNotFound("Provided id does not exists");
        }
        productRepository.deleteById(id);
    }

    private double convertHrkToEur(double hrkPrice, double rate){
        return Math.round((hrkPrice / rate) * 100.0) / 100.0;
    }

/*
    private float getHrkEurRate() throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity("https://api.hnb.hr/tecajn/v1?valuta=EUR", String.class);//Constants.HNB_EUR_URL, String.class);
        ObjectMapper mapper = new ObjectMapper();
        String  rateStr = mapper.readTree(response.getBody()).get(0).get(Constants.MIDDLE_RATE).asText();
        return Float.parseFloat( rateStr.replaceAll(",","\\."));
    }
    */


}
