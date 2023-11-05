package org.springframework.samples.petclinic.product;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = ProductController.class,		
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class Test8 {
    @MockBean
	ProductService ps;	    	

    @Autowired
	private MockMvc mockMvc;

    @WithMockUser(value = "spring")
    @Test
	void test8() throws Exception {			
		testGetValidProduct();
		testGetProductNotFound();
	}

	private void testGetProductNotFound() throws Exception {
		reset(ps);
		Integer productId=1;
		when(ps.getProductById(any(Integer.class))).thenReturn(null);
		mockMvc.perform(get("/api/v1/products/"+productId))
			.andExpect(status().isNotFound());		
		verify(ps).getProductById(productId);
	}

	private void testGetValidProduct() throws Exception {
		reset(ps);
		Integer productId=1;
		Product p=Test7.createProduct();		
		when(ps.getProductById(productId)).thenReturn(p);

		mockMvc.perform(get("/api/v1/products/"+productId))
				.andExpect(status().isOk())				
				.andExpect(jsonPath("$.name", is(p.getName())))
				.andExpect(jsonPath("$.price", is(p.getPrice())))
				.andExpect(jsonPath("$.type", is(p.getType().getName())));		
		verify(ps).getProductById(productId);
	}
}
