package com.hystrix.circuitbreaker.controller;

import com.ecommerce.microcommerce.model.Product;
import com.hystrix.circuitbreaker.delegate.ProductServiceDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductServiceController {

    @Autowired
    ProductServiceDelegate productServiceDelegate;

    /*
    @GetMapping("/Produits")
    public MappingJacksonValue getAllProducts() {
        System.out.println("ici1");
        return productServiceDelegate.callProductServiceAndGetAll();
    }
    */

    @GetMapping("/AdminProduits")
    public String getAllProductsAdmin() {
        return productServiceDelegate.callProductServiceAndGetAllAdmin();
    }

    @GetMapping("/ProduitsTrie")
    public List<Product> getAllProductsSorted() {
        return productServiceDelegate.callProductServiceAndGetAllSorted();
    }
}
