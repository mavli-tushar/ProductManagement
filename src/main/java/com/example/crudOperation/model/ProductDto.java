package com.example.crudOperation.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @NotEmpty(message = "name is required")
    private String name;
    @NotEmpty(message = "brnd is required")
    private String brand;
    @NotEmpty(message = "category is required")
    private String category;
    @Min(0)
    private double price;
    @Size(min=5, message = "discription should be at least 10 character")
    @Size(max=200, message = "discription cannot exceed 200 character")
    private String description;

    private MultipartFile img;

}
