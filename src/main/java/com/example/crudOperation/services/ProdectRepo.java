package com.example.crudOperation.services;

import com.example.crudOperation.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdectRepo extends JpaRepository<Product,Integer> {

}
