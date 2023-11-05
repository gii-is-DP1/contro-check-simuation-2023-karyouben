package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test3 extends ReflexiveTest{
    @Autowired(required = false)
    ProductRepository pr;    
    @Autowired
    EntityManager em;

    @Test
    public void test3(){
        testInitialProducts();
        testInitialProductTypes();
        testLinks();
    }



    public void testInitialProducts(){
        List<Product> offers=pr.findAll();
        assertTrue(offers.size()==2, "Exactly two Products should be present in the DB");
        
        Optional<Product> p1=pr.findById(1);
        assertNotNull(p1,"There should exist a Product with id:1");
        assertTrue(p1.isPresent(),"There should exist a Product with id:1");
        assertEquals("Wonderful dog collar",p1.get().getName());
        assertEquals(17,p1.get().getPrice());

        Optional<Product> p2=pr.findById(2); 
        assertNotNull(p2,"There should exist a Product with id:2");       
        assertTrue(p2.isPresent(),"There should exist a Product with id:2");
        assertEquals("Super Kitty Cookies",p2.get().getName());
        assertEquals(50,p2.get().getPrice());        
        

    }

    public void testInitialProductTypes()
    {
        ProductType pt1 = em.find(ProductType.class, 1);        
        ProductType pt2 = em.find(ProductType.class,2);
        assertNotNull(pt1);
        assertNotNull(pt2);        
        assertEquals("Accessories",pt1.getName());                            
        assertEquals("Food",pt2.getName());                                
            
    }




    private void testLinks() {
        checkLinkedById(Product.class,1,"getType",1,em);            
        checkLinkedById(Product.class,2,"getType",2,em);                    
    }



    
    
}
