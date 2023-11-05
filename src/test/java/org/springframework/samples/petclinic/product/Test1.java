package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToOne;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test1 extends ReflexiveTest{

    @Autowired(required = false)
    ProductRepository repo;
    
    @Autowired
    EntityManager em;

    

    @Test
    public void test1(){
        repositoryExists();
        repositoryContainsMethod();
        testConstraints();
    }


    public void repositoryExists(){
        assertNotNull(repo,"The repository was not injected into the tests, its autowired value was null");
    }


    public void repositoryContainsMethod(){
        if(repo!=null){
            Optional<Product> v=repo.findById(1000);
            assertFalse(v.isPresent(), "No result (null) should be returned for a product that does not exist");
        }else
            fail("The repository was not injected into the tests, its autowired value was null");
    }

    void testConstraints(){
        Map<String,List<Object>> invalidValues=Map.of(
                                            "price",     List.of(-1),
                                            "name", List.of("","  ")
                                            );


        Product p=createValidProduct(em);
        em.persist(p);
        
        checkThatFieldsAreMandatory(p, em, "name","price","type");        
        
        checkThatValuesAreNotValid(p, invalidValues,em);                

    }    

    public Product createValidProduct(EntityManager em){        
        ProductType pt=new ProductType();
        pt.setName("MyProductType");
        if(classIsAnnotatedWith(ProductType.class,Entity.class))
            em.persist(pt);
        Product p=new Product();
        p.setName("ValidProduct");
        p.setPrice(40);
        p.setType(pt);        
        return p;
    }
    
}
