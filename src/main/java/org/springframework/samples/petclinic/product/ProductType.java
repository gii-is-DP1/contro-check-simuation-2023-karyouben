package org.springframework.samples.petclinic.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.samples.petclinic.model.BaseEntity;

@Getter
@Setter
@Entity
public class ProductType extends BaseEntity {
    @NotNull
    @NotBlank
    @Size(min=3,max=50)
    @Column(unique = true)
    String name;
}
