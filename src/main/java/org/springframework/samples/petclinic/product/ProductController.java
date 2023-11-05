package org.springframework.samples.petclinic.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    
    @Autowired
    ProductService ps;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody @Valid Product p) throws UnfeasibleProductUpdate{
        ps.save(p);
        return p;
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") Integer id){
        Product p=ps.getProductById(id);
        if(p==null)
            throw new ResourceNotFoundException("Product","id",id);
        return p;
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable("id") Integer id,@RequestBody @Valid Product p) throws UnfeasibleProductUpdate{
        Product pToUpdate=getProduct(id);
        ps.save(p);        
    }
}
