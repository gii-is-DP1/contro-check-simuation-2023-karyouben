package org.springframework.samples.petclinic.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Test9 {
    
    @MockBean
	ProductService ps;
    @Autowired
    private WebApplicationContext context;        
    private MockMvc mockMvc;
    
    private static final Integer A_PRODUCT_ID = 1;        

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void test9()  throws Exception {
        testProductUpdateControllerOK();
        testProductUpdateControllerInvalid();        
    }

    void testProductUpdateControllerOK() throws Exception{
        Product p=Test7.createProduct();        
        p.setPrice(2);
        reset(ps);
        when(ps.getProductById(A_PRODUCT_ID)).thenReturn(p);
        when(ps.getAllProducts()).thenReturn(List.of(p));
        when(ps.getProductType("Food")).thenReturn(p.getType());

        ObjectMapper objectMapper = new ObjectMapper();

        String json = "{"                                   + //
                        "\"id\": 1,"                          + //
                        "\"name\": \"Wonderful dog coat\","   + //
                        "\"price\": 17,"                      + //
                        "\"type\": \"Food\""                    + //
                      "}";


        mockMvc.perform(put("/api/v1/products/"+A_PRODUCT_ID)                            
                            .contentType(MediaType.APPLICATION_JSON)                            
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        verify(ps).save(any(Product.class));    
    }

    void testProductUpdateControllerInvalid() throws Exception{
        Product p=Test7.createProduct();        
        p.setPrice(2);
        reset(ps);        
        // WE SIMULATE THAT PRODUCT WITH ID 1 DOES NOT EXIST:
        when(ps.getProductById(A_PRODUCT_ID)).thenReturn(null);
        when(ps.getAllProducts()).thenReturn(List.of());
        when(ps.getProductType("Food")).thenReturn(p.getType());
        
        String json = "{"                                   + //
                        "\"id\": 1,"                          + //
                        "\"name\": \"Wonderful dog coat\","   + //
                        "\"price\": 17,"                      + //
                        "\"type\": \"Food\""                    + //
                      "}";


        mockMvc.perform(put("/api/v1/products/"+A_PRODUCT_ID)                                                        
                            .contentType(MediaType.APPLICATION_JSON)                            
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());        
        verify(ps,times(0)).save(any(Product.class));
        
        when(ps.getProductById(A_PRODUCT_ID)).thenReturn(p);
       json ="{"                                   + //
                        "\"id\": 1,"                          + //
                        "\"name\": \"Wonderful dog coat\","   + //
                        "\"price\": -17,"                      + // THIS HAS CHANGED AND IS INVALID!!!
                        "\"type\": \"Food\""                    + //
                      "}"; 
        mockMvc.perform(put("/api/v1/products/"+A_PRODUCT_ID)                            
                            .contentType(MediaType.APPLICATION_JSON)                            
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());        
        verify(ps,times(0)).save(any(Product.class));

    }		
	    
}


