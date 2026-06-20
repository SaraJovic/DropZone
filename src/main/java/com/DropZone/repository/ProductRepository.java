package com.DropZone.repository;

import com.DropZone.entity.Product;
import com.DropZone.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByGender(Gender gender);
    List<Product> findByCategoryIdAndGender(Long categoryId, Gender gender);
}
