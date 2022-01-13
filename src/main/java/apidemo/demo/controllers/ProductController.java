package apidemo.demo.controllers;

import apidemo.demo.models.Product;
import apidemo.demo.models.ResponseObject;
import apidemo.demo.repositories.ProductRepository;
import apidemo.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/products")
public class ProductController {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private ProductService productService;

    @GetMapping("")
    List<Product> getAllProducts() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Product> product = repository.findByIdAndIsDeleteIsFalse(id);
        if (product.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Query product successfully", product)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("false", "Cannot find product with id = " + id, "")
            );
        }
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct) {
        List<Product> products = repository.findByNameAndIsDeleteIsFalse(newProduct.getName().trim());
        if (products.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Product name already exist", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert Product Successfully", repository.save(newProduct))
        );

    }

    @PutMapping("/update/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product updateProduct, @PathVariable Long id) {
        Optional<Product> oldProduct = repository.findById(id);
        if (!oldProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("false", "Cannot find product with id = " + id, "")
            );
        }
        oldProduct.map(product -> {
            product.setName(updateProduct.getName());
            product.setPrice(updateProduct.getPrice());
            product.setUrl(updateProduct.getUrl());
            product.setYear(updateProduct.getYear());
            return repository.save(product);
        }).orElseGet(() -> {
            updateProduct.setId(id);
            return repository.save(updateProduct);
        });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update Product Successfully", oldProduct)
        );
    }


}
