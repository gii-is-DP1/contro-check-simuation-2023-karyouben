package org.springframework.samples.petclinic.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product,Integer> {
    Optional<Product> findById(Integer id);

    List<Product> findAll();

    @Query("SELECT p FROM Product p WHERE p.price<?1")
    List<Product> findByPriceLessThan(Integer valu);

    Product save(Product p);


    @Query("SELECT pt FROM ProductType pt WHERE pt.name=?1")
    ProductType findProductTypeByName(String name);

    @Query("SELECT pt FROM ProductType pt")
    List<ProductType> findAllProductTypes();

    
}
