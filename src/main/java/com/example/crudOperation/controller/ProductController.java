package com.example.crudOperation.controller;

import com.example.crudOperation.model.Product;
import com.example.crudOperation.model.ProductDto;
import com.example.crudOperation.services.ProdectRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.*;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProdectRepo prodectRepo;
    @GetMapping({"", "/"})
    public String showproducts(Model model){
        List<Product>products=prodectRepo.findAll(Sort.by(Sort.Direction.DESC,"id"));
        model.addAttribute("products",products);
        return  "index";
    }
    @GetMapping("/create")
    public String showCreaterodProduct(Model model){
        ProductDto productDto=new ProductDto();
        model.addAttribute("productDto",productDto);
        return "CreateProduct";
    }

    @PostMapping("/create")

    public String createProduct(
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result){
        if (productDto.getImg().isEmpty()){
            result.addError(new FieldError("productDto","img","the image file is required"));
        }

        if (result.hasErrors()){
            return "CreateProduct";
        }

        //save image

        MultipartFile img=productDto.getImg();
        String fileName=img.getOriginalFilename();

        try{
            String uploadDir="public";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);

            try(InputStream inputStream=img.getInputStream()){
                Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        }catch (Exception e ){
            System.out.println("Exception"+e.getMessage());
        }

        Product product=new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setImg(fileName);

        prodectRepo.save(product);

        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String editProduct(Model model , @RequestParam int id){
        try{
            Product product=prodectRepo.findById(id).get();
            model.addAttribute("product",product);


            ProductDto productDto = new ProductDto();
            productDto.setBrand(product.getBrand());
            productDto.setName(product.getName());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());
            productDto.setCategory(product.getCategory());

            model.addAttribute("productDto",productDto);


        }catch (Exception e){
            System.out.println("Exception: "+ e.getMessage());
            return "redirect:/products";
        }
        return "EditProduct";
    }

    @PostMapping("/edit")
    public String updateProduct(Model model, @RequestParam int id,
                                @Valid @ModelAttribute ProductDto productDto,
                                BindingResult result){

        try {
            Product product=prodectRepo.findById(id).get();
            model.addAttribute("product",product);

            if (result.hasErrors()){
                return "EditProduct";
            }

            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());

            prodectRepo.save(product);

        }catch (Exception e){
            System.out.println("Exception: "+e.getMessage());
        }
        return"redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id){
        try{
            Product product=prodectRepo.findById(id).get();
            Path imgpath =Paths.get("public/"+ product.getImg());
            try {
                Files.delete(imgpath);
            }catch (Exception e){
                System.out.println("Exception: " + e.getMessage());
            }
            prodectRepo.delete(product);
        }catch (Exception e){
            System.out.println("Exception: "+e.getMessage());
        }
        return "redirect:/products";
    }
}
