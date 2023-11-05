package org.springframework.samples.petclinic.product;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;



@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test6 extends ReflexiveTest {

    @Autowired
    EntityManager em;
    @Autowired
    ProductService ps;

    @Test
    public void test6(){                
        testProductCheaperThan();
        testTransactionality();
        
    }

    private void testTransactionality() {        
        checkTransactional(ProductService.class,"getProductsCheaperThan", Integer.class);
    }

    public void testProductCheaperThan(){
        assertTrue(ps.getProductsCheaperThan(0).isEmpty());
        Product p=createNewCheapProduct();
        List<Product> products=ps.getProductsCheaperThan(p.getPrice()+1);
        assertFalse(products.isEmpty());
        assertTrue(products.contains(p));
    }

    

    private Product createNewCheapProduct() {
        ProductType petCandy=new ProductType();
        petCandy.setName("Pet candy");
        if(classIsAnnotatedWith(ProductType.class, Entity.class)){
            em.persist(petCandy);
        }
        Product p=new Product();
        p.setType(petCandy);
        p.setName("Dog marshmallow");
        p.setPrice(1);
        if(classIsAnnotatedWith(Product.class, Entity.class)){
            em.persist(p);
        }
        return p;
    }        


}

