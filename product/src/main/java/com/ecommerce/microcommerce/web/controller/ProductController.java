package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitGratuitException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Api(value = "ProductController", description = "Api pour recuperer les produits d'une base de donnée, ajouter, modifier et suprimer")
@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;


    //Récupérer la liste des produits
    @ApiOperation(value = "Retourne la liste de tous les produits")
    @RequestMapping(value = "/Produits", method = RequestMethod.GET)
    public MappingJacksonValue listeProduits() {
        Iterable<Product> produits = productDao.findAll();
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
        produitsFiltres.setFilters(listDeNosFiltres);
        return produitsFiltres;
    }

    @ApiOperation(value = "Retourne la liste de tous les produits avec affichage de la marge")
    @GetMapping("/AdminProduits")
    public String afficherVueAdmin() {
        Iterable<Product> produits = productDao.findAll();
        StringBuilder res = new StringBuilder("} <br>");

        for (Product produit:
             produits) {
            res.append(produit.toString()).append(" : ").append(produit.calculerMargeProduit()).append(", <br>");
        }

        res.append("}");

        return res.toString();
    }

    @ApiOperation(value = "Retourne la liste de tous les produits triés sur les noms dans l'ordre alphabétique")
    @GetMapping("/ProduitsTrie")
    public List<Product> trierProduitsParOrdreAlphabetique() {
        return productDao.findAllByOrderByNomAsc();
    }


    //Récupérer un produit par son Id
    @ApiOperation(value = "retourne un produit grace à son Id")
    @GetMapping("getProduitId/{id}")
    public Product afficherUnProduit(@PathVariable(value = "id") int id) {

        return productDao.findById(id);
    }


    //ajouter un produit
    @ApiOperation(value = "Ajoute un produit passé en Post dans la base")
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {

        if (product.getPrix() == 0) {
            throw new ProduitGratuitException("Le prix du nouveau produit est gratuit");
        } else {
            Product productAdded =  productDao.save(product);

            if (productAdded == null)
                return ResponseEntity.noContent().build();

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(productAdded.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        }
    }

    @ApiOperation(value = "Suprime un produit de la base grace à son id")
    @RequestMapping(value = "/suprimerProduit/{id}", method = RequestMethod.GET)
    public void supprimerProduit(@PathVariable(value = "id") int id) {
        productDao.delete(id);
    }


    @ApiOperation(value = "Met à jour un produit en base")
    @RequestMapping(value = "/updateProduit", method = RequestMethod.PUT)
    public void updateProduit(@RequestBody Product product) {
        if (product.getPrix() == 0) {
            throw new ProduitGratuitException("Le nouveau prix du produit est gratuit");
        } else {
            productDao.save(product);
        }
    }


    //Pour les tests
    @GetMapping(value = "test/produits/{prix}")
    public List<Product>  testeDeRequetes(@PathVariable int prix) {
        return productDao.chercherUnProduitCher(400);
    }



}
