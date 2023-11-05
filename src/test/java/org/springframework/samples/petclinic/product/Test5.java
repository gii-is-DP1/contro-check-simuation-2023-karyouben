package org.springframework.samples.petclinic.product;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test5 extends ReflexiveTest{
    
    @Autowired
    EntityManager em;    

    @Autowired
    ProductService ps;
    

    @Test
    public void test5() {
        validatefindByComplexCriteria();
    }
    // List<Watch> findOverlappedWatches(LocaDate date, LocalTime time, int vetId);
    
    @Transactional
    private void validatefindByComplexCriteria() {
        ProductType pt=Test2.createValidProductType();
        ProductType found=null;
        if(classIsAnnotatedWith(ProductType.class,Entity.class)){
                em.persist(pt);
                found=ps.getProductType(pt.getName());
                assertNotNull(found,"I cannot find a product that I have just persisted");                                
        }
        found=ps.getProductType("This is a product name that does not exist");
        assertNull(found);        
    }

}
