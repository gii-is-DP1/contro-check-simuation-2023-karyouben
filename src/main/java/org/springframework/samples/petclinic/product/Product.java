package org.springframework.samples.petclinic.product;

import org.springframework.samples.petclinic.model.NamedEntity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product extends NamedEntity {    
    @Min(0)
    @NotNull
    Integer price;
    @NotNull
    @JsonSerialize(using=ProductTypeSerializer.class)
    @JsonDeserialize(using=ProductTypeDeserializer.class)
    @ManyToOne
    ProductType type;
}
