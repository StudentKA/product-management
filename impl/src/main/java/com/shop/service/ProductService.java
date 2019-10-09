package com.shop.service;

import com.shop.api.swagger.models.ProductDto;
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

    public ProductDto getProduct(String id){
        log.info("Get product by id {}", id);
        Product product = repository.findById(id).orElse(null);
        if (product != null)
            return convertToDto(product);
        else
            throw new ProductNotFoundException(id);
    }

    private ProductDto convertToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.id(product.getId());
        productDto.type(product.getType());
        productDto.name(product.getName());
        productDto.cost(product.getCost());
        productDto.amount(product.getAmount());
        return productDto;
    }

    public ProductDto createProduct(ProductDto productDto) {
        log.info("Create new product");
        Product product = convertToEntity(productDto);
        repository.save(product);
        return convertToDto(product);
    }

    private Product convertToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setType(productDto.getType());
        product.setName(productDto.getName());
        product.setCost(productDto.getCost());
        product.setAmount(productDto.getAmount());
        return product;
    }

    public void deleteProduct(String id){
        log.info("Delete product with id {}", id);
        Product product = repository.findById(id).orElse(null);
        if (product == null)
            throw new ProductNotFoundException(id);
        repository.delete(product);
    }

    public List<ProductDto> getAllProducts(){
        log.info("Get all products");
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::convertToDto).collect(Collectors.toList());
    }

    public ProductDto updateProduct(String id, ProductDto productDto){
        log.info("Update product with id {}", productDto.getId());
        Product product = repository.findById(id).orElse(null);
        if (product == null)
            throw new ProductNotFoundException(id);
        if (productDto.getType() != null)
            product.setType(productDto.getType());
        if (productDto.getName() != null)
            product.setName(productDto.getName());
        if (productDto.getCost() != null)
            product.setCost(productDto.getCost());
        if (productDto.getAmount() != null)
            product.setAmount(productDto.getAmount());
        repository.save(product);
        return convertToDto(product);
    }
}
