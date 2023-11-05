package org.springframework.samples.petclinic.product;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    ProductRepository pr;

    @Autowired
    public ProductService(ProductRepository pr){
        this.pr=pr;
    }

    @Transactional(readOnly=true)
    public List<Product> getAllProducts(){
        return pr.findAll();
    }
    
    @Transactional(readOnly=true)
    public Product getProductById(Integer id){
        Optional<Product> p=pr.findById(id);
        return p.isPresent()?p.get():null;
    }  

    @Transactional()
    public void save(Product p) throws UnfeasibleProductUpdate{
        if(p.getId()!=null){
            Product pToUpdate=getProductById(p.getId());
            if(pToUpdate!=null)
                if(p.getPrice()>pToUpdate.getPrice()*2)
                    throw new UnfeasibleProductUpdate();
        }
        pr.save(p);
    }

    @Transactional(readOnly=true)
    public ProductType getProductType(String name){
        return pr.findProductTypeByName(name);
    }

    @Transactional(readOnly=true)
    public List<Product> getProductsCheaperThan(Integer value){
        return pr.findByPriceLessThan(value);
    }
}
