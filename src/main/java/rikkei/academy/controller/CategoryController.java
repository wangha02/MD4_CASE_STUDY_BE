package rikkei.academy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rikkei.academy.dto.response.ResponseMessage;
import rikkei.academy.model.Category;
import rikkei.academy.model.User;
import rikkei.academy.security.userprincipal.UserDetailServiceIMPL;
import rikkei.academy.service.category.ICategoryService;
import rikkei.academy.service.user.IUSerService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@CrossOrigin
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;
    @Autowired
    UserDetailServiceIMPL userDetailServiceIMPL;

    @Autowired
    IUSerService userService;

    @GetMapping
    public ResponseEntity<?> getList() {
        Iterable<Category> listCategories = categoryService.findAll();
        return new ResponseEntity<>(listCategories, HttpStatus.OK);
    }

    @PostMapping

    public ResponseEntity<?> createCategory(@RequestBody Category category){
//        User currentUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        categoryService.save(category);
        return new ResponseEntity<>(new ResponseMessage("create success"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detailCategory(@PathVariable("id") Category category) {
        return category == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category category1 = categoryService.findById(id);
        category1.setName(category.getName());
        categoryService.save(category1);
        return new ResponseEntity<>(new ResponseMessage("Update success!!!"), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        Category category = categoryService.findById(id);

        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        categoryService.deleteById(id);
        return new ResponseEntity<>(new ResponseMessage("Delete Success!"), HttpStatus.OK);
    }
}
