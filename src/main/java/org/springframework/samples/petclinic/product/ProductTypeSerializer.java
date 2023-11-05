package org.springframework.samples.petclinic.product;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ProductTypeSerializer extends JsonSerializer<ProductType>{

    @Override
    public void serialize(ProductType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getName());
    }

}
