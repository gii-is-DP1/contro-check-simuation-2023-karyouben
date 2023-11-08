package org.springframework.samples.petclinic.product;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.pet.PetType;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product,Integer> {
    Optional<Product> findById(Integer id);

    List<Product> findAll();

    Product save(Product p);

    @Query("SELECT pt FROM ProductType pt")
    List<ProductType> findAllProductTypes();

    @Query("SELECT pt FROM ProductType pt WHERE pt.name= :name")
    ProductType findProductTypeByName(String name);

    @Query("SELECT p FROM Product p WHERE p.price< :price")
    List<Product> findByPriceLessThan(Integer price);
}
