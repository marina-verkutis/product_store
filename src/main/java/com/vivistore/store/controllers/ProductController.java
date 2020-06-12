package com.vivistore.store.controllers;

import com.vivistore.store.models.Product;
import com.vivistore.store.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public String products(Model model) {
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/products/add")
    public String add(Model model) {
        return "product_add";
    }

    @PostMapping("/products/add")
    public String productAdd(@RequestParam String name, @RequestParam String price, @RequestParam String amount, Model model) {
        String priceCleaned =  price.replaceAll("[^\\d]", "");
        Product product = new Product(name, Integer.parseInt (priceCleaned), Integer.parseInt (amount));
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/update")
    public String productUpdate(@PathVariable(value = "id") long id, Model model) {
        if(!productRepository.existsById(id)){
            return "redirect:/products";
        }

        Optional<Product> product = productRepository.findById(id);
        ArrayList<Product> pr = new ArrayList<>();
        product.ifPresent(pr::add);
        model.addAttribute("product", pr);
        return "product-edit";
    }

    @PostMapping("/products/{id}/update")
    public String productUpdate(@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam String price, @RequestParam String amount, Model model) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setName(name);
        String priceCleaned =  price.replaceAll("[^\\d]", "");
        product.setPrice(Integer.parseInt (priceCleaned));
        product.setAmount(Integer.parseInt (amount));
        productRepository.save(product);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/remove")
    public String productDelete(@PathVariable(value = "id") long id, Model model) {
        Product product = productRepository.findById(id).orElseThrow();
        productRepository.delete(product);
        return "redirect:/products";
    }

}
