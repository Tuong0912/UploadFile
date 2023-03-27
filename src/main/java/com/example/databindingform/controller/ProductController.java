package com.example.databindingform.controller;

import com.example.databindingform.model.Product;
import com.example.databindingform.model.ProductForm;
import com.example.databindingform.service.IProductService;
import com.example.databindingform.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Value("${file-upload}")
    private String fileUpload;

    private final IProductService productService = new ProductService();

    @GetMapping("")
    public String index(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("create")
    public String createForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        return "product/create";
    }

    @PostMapping("/save")
    public String createProduct(@ModelAttribute ProductForm productForm, Model model) {
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Product product = new Product(productForm.getId(),
                productForm.getName(),
                productForm.getDescription(),
                fileName);
        productService.save(product);
        model.addAttribute("productForm", productForm);
        return "redirect:/product";
    }

//    @GetMapping("{id}/edit")
//    public String editForm(Model model, @PathVariable int id) {
//        model.addAttribute("product", this.productService.findById(id));
//        return "product/edit";
//    }
//
//    @PostMapping("/editProduct")
//    public String edit(@ModelAttribute ProductForm productForm) {
//        MultipartFile multipartFile = productForm.getImage();
//        String fileName = multipartFile.getOriginalFilename();
//        try {
//            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        productForm.setName(productForm.getName());
//        productForm.setDescription(productForm.getDescription());
//        productForm.setImage(productForm.getImage());
//        this.productService.update(productForm.getId(), productForm);
//    }

}


//        MultipartFile multipartFile = productForm.getImage();
//        String fileName = multipartFile.getOriginalFilename();
//        try {
//            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        Product product = new Product(productForm.getId(),
//                productForm.getName(),
//                productForm.getDescription(),
//                fileName);
//        productService.save(product);
//        ModelAndView modelAndView = new ModelAndView("product/create");
//        List<Product> products = productService.findAll();
//        modelAndView.addObject("products", products);
