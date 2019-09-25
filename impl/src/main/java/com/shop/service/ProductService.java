package com.shop.service;

import com.shop.api.swagger.models.ProductDTO;
import com.shop.exception.ProductNotFoundException;
import com.shop.model.Product;
import com.shop.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class ProductService {
    private ProductsRepository repository;

    @Autowired
    public ProductService(ProductsRepository repository){
        this.repository = repository;
    }

    public ProductDTO getProduct(String id){
        log.info("Get product by id {}", id);
        Product product = repository.findById(id).orElse(null);
        if (product != null)
            return convertToDTO(product);
        else
            throw new ProductNotFoundException(id);
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.id(product.getId());
        productDTO.type(product.getType());
        productDTO.name(product.getName());
        productDTO.cost(product.getCost());
        productDTO.amount(product.getAmount());
        return productDTO;
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        log.info("Create new product");
        Product product = convertToEntity(productDTO);
        repository.save(product);
        return convertToDTO(product);
    }

    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setType(productDTO.getType());
        product.setName(productDTO.getName());
        product.setCost(productDTO.getCost());
        product.setAmount(productDTO.getAmount());
        return product;
    }

    public void deleteProduct(String id){
        log.info("Delete product with id {}", id);
        Product product = repository.findById(id).orElse(null);
        if (product == null)
            throw new ProductNotFoundException(id);
        repository.delete(product);
    }

    public List<ProductDTO> getAllProducts(){
        log.info("Get all products");
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public ProductDTO updateProduct(String id, ProductDTO productDTO){
        log.info("Update product with id {}", productDTO.getId());
        Product product = repository.findById(id).orElse(null);
        if (product == null)
            throw new ProductNotFoundException(id);
        if (productDTO.getType() != null)
            product.setType(productDTO.getType());
        if (productDTO.getName() != null)
            product.setName(productDTO.getName());
        if (productDTO.getCost() != null)
            product.setCost(productDTO.getCost());
        if (productDTO.getAmount() != null)
            product.setAmount(productDTO.getAmount());
        repository.save(product);
        return convertToDTO(product);
    }
}