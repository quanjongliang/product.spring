package apidemo.demo.repositories;

import apidemo.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //    @Query(value = "Select * from ")
    List<Product> findByNameAndIsDeleteIsFalse(String name);

    Optional<Product> findByIdAndIsDeleteIsFalse(Long name);
}
