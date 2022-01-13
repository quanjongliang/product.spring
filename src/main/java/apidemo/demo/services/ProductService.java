package apidemo.demo.services;

import apidemo.demo.models.Product;
import apidemo.demo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    List<Product> findAllProduct() {
        return this.productRepository.findAll();
    }

}
