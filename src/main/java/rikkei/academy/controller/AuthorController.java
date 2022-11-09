package rikkei.academy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rikkei.academy.dto.response.ResponseMessage;
import rikkei.academy.model.Author;
import rikkei.academy.model.User;
import rikkei.academy.security.userprincipal.UserDetailServiceIMPL;
import rikkei.academy.service.author.IAuthorService;
import rikkei.academy.service.comic.IComicService;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin
@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    @Autowired
    IAuthorService authorService;
    @Autowired
    UserDetailServiceIMPL userDetailServiceIMPL;
    @Autowired
    IComicService comicService;

    @GetMapping
    public ResponseEntity<?> showListAuthor(){
        Iterable<Author> listAuthor = authorService.findAll();
        return new  ResponseEntity<>(listAuthor, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?>createAuthor(@RequestBody Author author){
        authorService.save(author);
        return new ResponseEntity<>(new ResponseMessage("Created"), CREATED);

    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editAuthor(
            @PathVariable("id") Author oldAuthor,
            @RequestBody Author newAuthor
    ){
        if (oldAuthor == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        oldAuthor.setName(newAuthor.getName());
        authorService.save(oldAuthor);
        return ResponseEntity.ok(oldAuthor);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?>deleteAuthor(@PathVariable("id") Author author){
        if (author == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        authorService.deleteById(author.getId());
        return ResponseEntity.ok(new ResponseMessage("Deleted"));
    }

}
