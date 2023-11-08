package org.springframework.samples.petclinic.product;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    ProductService ps;
    @Transactional
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody @Valid Product p) throws UnfeasibleProductUpdate {
        ps.save(p);
        return p;
    }
    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProduct(@PathVariable("id") Integer value){
        Product p = ps.getProductById(value);
        if(p==null)
            throw  new ResourceNotFoundException("No encuentro el producto que" +
                    " me has pedido con id: " + value);
        return new ProductDTO(p);
    }


    @Transactional
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void putProduct(@PathVariable("id") Integer value,@RequestBody @Valid ProductDTO pr) throws UnfeasibleProductUpdate {
        Product p= ps.getProductById(value);
        if(p==null)
            throw  new ResourceNotFoundException("No encuentro el producto que" +
                    " me has pedido con id: " + value);
        p.setName(p.getName());
        ProductType pt= ps.getProductType(p.getName());
        p.setPrice(p.getPrice());
        p.setType(pt);
        ps.save(p);

    }


    
}
