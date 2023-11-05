package org.springframework.samples.petclinic.product;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@Component
public class ProductTypeDeserializer extends JsonDeserializer<ProductType> {
    @Autowired
    ProductService ps;

    @Override
    public ProductType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException{
        ProductType pt=ps.getProductType(p.getText());
        return pt;
    }

    
    
}
