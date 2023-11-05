package org.springframework.samples.petclinic.product;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(MockitoExtension.class)
public class Test4 extends ReflexiveTest {
    @Mock
    ProductRepository pr;        
    
    ProductService ps;        
    
    @BeforeEach
    public void configuation(){
        ps=new ProductService(pr);        
    }

    @Test 
    public void test4() {
        productServiceIsInjected();
        productServiceCanGetProducts();        
        prouctServiceCanSaveProducts();        
        checkTransactionalityOfProductService();        
        
    }

    public void checkTransactionalityOfProductService(){
        checkTransactional(ProductService.class,"save", Product.class);        
        checkTransactional(ProductService.class,"getAllProducts");
    }

    
    
    public void productServiceIsInjected() {
        assertNotNull(ps,"OfferService was not injected by spring");       
    }
    
    public void productServiceCanGetProducts(){
        assertNotNull(ps,"ProductService was not injected by spring");
        when(pr.findAll()).thenReturn(List.of());
        List<Product> products=ps.getAllProducts();
        assertNotNull(products,"The list of Products found by the OfferService was null, ¿are you using the respository method findAll to get the products? (You should!)");
        // The test fails if the service does not invoke the findAll of the repository:
        verify(pr).findAll();            
    }

   

    

    private void prouctServiceCanSaveProducts() {        
        assertNotNull(ps,"ProductService was not injected by spring");
        when(pr.save(any(Product.class))).thenReturn(null);
        Product p=new Product();
        p.setName("ValidProductName");
        p.setPrice(5);
        try {
            ps.save(p);
        } catch (UnfeasibleProductUpdate e) {
            fail("The exception should not be thrown.");
        }
        // The test fails if the service does not invoke the save function of the repository:
        verify(pr).save(p);                
    }

        



}
