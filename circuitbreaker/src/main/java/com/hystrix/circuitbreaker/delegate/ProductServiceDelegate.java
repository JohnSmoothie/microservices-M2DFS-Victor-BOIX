package com.hystrix.circuitbreaker.delegate;

import com.ecommerce.microcommerce.model.Product;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProductServiceDelegate {

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    /*
    @HystrixCommand(fallbackMethod = "callProductServiceAndGetAll_Fallback")
    public MappingJacksonValue callProductServiceAndGetAll() {
        System.out.println("ici1");
        ResponseEntity<MappingJacksonValue> produits = restTemplate.exchange("http://localhost:9090/Produits", HttpMethod.GET, null, MappingJacksonValue.class);
        System.out.println("ici2");
        return produits.getBody();

    }
     */
    @HystrixCommand(fallbackMethod = "callProductServiceAndGetAll_Fallback")
    public String callProductServiceAndGetAllAdmin() {
        ResponseEntity<String> produits = restTemplate.exchange("http://localhost:9090/AdminProduits", HttpMethod.GET, null, String.class);
        return  produits.getBody();
    }

    public String callProductServiceAndGetAllAdmin_Fallback() {
        return "Micro service Produit non accesible";
    }

    @HystrixCommand(fallbackMethod = "callProductServiceAndGetAll_Fallback")
    public List<Product> callProductServiceAndGetAllSorted() {
        ResponseEntity<ListProduct> produits = restTemplate.exchange("http://localhost:9090/ProduitsTrie", HttpMethod.GET, null, ListProduct.class);
        return  produits.getBody();
    }

    public abstract static class ListProduct implements List<Product> {}

}

