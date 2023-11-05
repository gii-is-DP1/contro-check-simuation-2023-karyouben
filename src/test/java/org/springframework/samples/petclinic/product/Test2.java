package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@DataJpaTest
public class Test2 extends ReflexiveTest {
    @Autowired(required = false)
    ProductRepository repo;    
    @Autowired(required = false)
    EntityManager em;    

    @Test    
    public void test2(){
        repositoryExists();
        testFindAllProductTypes();     
        testConstraints();
        
    }




    private void testFindAllProductTypes() {
        invokeMethodReflexively(repo, "findAllProductTypes");
    }




    public void repositoryExists() {
            if(repo==null)
                fail("The repository was not injected into the tests, its autowired value was null");
    }


    public void testConstraints(){        
        
        ProductType p=createValidProductType();
        em.persist(p);

        checkThatFieldsAreMandatory(p,em, "name");

        checkThatValueIsNotValid(p, "name","", em);
        
        checkThatValueIsNotValid(p, "name"," ", em);
        // We check that name is unique:
        ProductType p2=new ProductType();
        p2.setName(p.getName());
        assertThrows(Exception.class, ()->{ 
            em.persist(p2);
            em.flush();
        });
    }    

    public static ProductType createValidProductType(){
        ProductType pt=new ProductType();
        pt.setName("AValidProductType");
        return pt;
    }
}
