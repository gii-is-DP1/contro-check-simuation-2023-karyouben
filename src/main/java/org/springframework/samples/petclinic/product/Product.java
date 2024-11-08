package org.springframework.samples.petclinic.product;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.samples.petclinic.model.BaseEntity;

@Getter
@Setter
@Entity
public class Product extends BaseEntity {
    @NotNull
    @NotBlank
    @Size(min=3,max=50)
    String name;

    @NotNull
    @Min(0)
    Integer price;

    @NotNull
    @ManyToOne
    ProductType type;
}
