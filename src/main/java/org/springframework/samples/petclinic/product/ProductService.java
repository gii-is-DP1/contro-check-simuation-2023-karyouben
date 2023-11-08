package org.springframework.samples.petclinic.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    ProductRepository pr;
    @Autowired
    public ProductService(ProductRepository pr){
        this.pr=pr;
    }
   @Transactional(readOnly = true)
    public List<Product> getAllProducts(){
        return pr.findAll();
    }
    @Transactional(readOnly = true)
    public Product getProductById(Integer id){
        Optional<Product> p =pr.findById(id);

        return p.isEmpty()?null:p.get();
    }
    @Transactional
    public void save(Product p) throws UnfeasibleProductUpdate{
        Product oldProduct = getProductById(p.getId());
        if(oldProduct!=null)
            if(p.getPrice()>2* oldProduct.getPrice())
                throw new UnfeasibleProductUpdate();
        pr.save(p);
    }
    @Transactional(readOnly = true)
    public ProductType getProductType(String name){
        return pr.findProductTypeByName(name);
    }
    @Transactional(readOnly = true)
    public List<Product> getProductsCheaperThan(Integer value){
        return pr.findByPriceLessThan(value);
    }
}
