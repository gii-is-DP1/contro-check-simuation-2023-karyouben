package org.springframework.samples.petclinic.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(value = ProductController.class,		
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class Test7 {
    

    @MockBean
    ProductService ps;
    
    @Autowired
    private MockMvc mockMvc;

    public static String A_PRODUCT_NAME ="New product name";
    public static Integer A_PRODUCT_PRICE =12;


    @Test
    @WithMockUser(value = "spring", authorities = {"admin"})    
    public void test7() throws Exception{  
        testProductCreationControllerOK();
        testProductCreationControllerInvalid();                            
    }


    private void testProductCreationControllerInvalid() throws Exception {                
        Product p=createProduct();
        reset(ps);
        when(ps.getProductType(p.getType().getName())).thenReturn(p.getType());
        ObjectMapper objectMapper = new ObjectMapper();        
        // Test with invalid price
        p.setPrice(-1);
        String json = objectMapper.writeValueAsString(p);
        p.setPrice(A_PRODUCT_PRICE);
        mockMvc.perform(post("/api/v1/products")
                            .with(csrf())                            
                            .contentType(MediaType.APPLICATION_JSON)                            
                            .content(json))
                .andExpect(status().isBadRequest());                
        verify(ps,never()).save(any(Product.class));
        // Test with invalid name
        p.setName("");
        json = objectMapper.writeValueAsString(p);
        p.setName(A_PRODUCT_NAME);
        mockMvc.perform(post("/api/v1/products")
                            .with(csrf())                            
                            .contentType(MediaType.APPLICATION_JSON)                            
                            .content(json))
                .andExpect(status().isBadRequest());                
        verify(ps,never()).save(any(Product.class));
        // Test with invalid PetType
        when(ps.getProductType(any(String.class))).thenReturn(null);
        p.setType(null);
        json = objectMapper.writeValueAsString(p);        
        mockMvc.perform(post("/api/v1/products")
                            .with(csrf())                            
                            .contentType(MediaType.APPLICATION_JSON)                            
                            .content(json))
                .andExpect(status().isBadRequest());                
        verify(ps,never()).save(any(Product.class));
    }


    private void testProductCreationControllerOK() throws Exception {
        Product p=createProduct();
        
        reset(ps);
        when(ps.getProductType(p.getType().getName())).thenReturn(p.getType());
        
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(p);

        mockMvc.perform(post("/api/v1/products")
                            .with(csrf())                            
                            .contentType(MediaType.APPLICATION_JSON)                            
                            .content(json))
                .andExpect(status().isCreated());
        verify(ps).save(any(Product.class));                
    }
    
    public static Product createProduct(){
        Product p=new Product();        
        p.setType(Test2.createValidProductType());
        p.setName(A_PRODUCT_NAME);
        p.setPrice(A_PRODUCT_PRICE);
        return p;
    }
    
 
}