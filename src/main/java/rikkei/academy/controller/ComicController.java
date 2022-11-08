package rikkei.academy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rikkei.academy.dto.request.ComicDTO;
import rikkei.academy.dto.response.ResponseMessage;
import rikkei.academy.model.Category;
import rikkei.academy.model.Comic;
import rikkei.academy.model.User;
import rikkei.academy.security.userprincipal.UserDetailServiceIMPL;
import rikkei.academy.service.category.ICategoryService;
import rikkei.academy.service.comic.IComicService;

import java.util.Optional;

@RestController
@RequestMapping("/api/comic")
@CrossOrigin
public class ComicController {
    @Autowired
    private IComicService comicService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    UserDetailServiceIMPL userDetailServiceIMPL;

    @GetMapping
    public ResponseEntity<?> findAllComic(@PageableDefault(size = 10) Pageable pageable) {
        Page<Comic> comics = comicService.findAll(pageable);
        return new ResponseEntity<>(comics, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createComic(@RequestBody ComicDTO comicDTO) {
        Comic comic = new Comic();
        User currentUser = userDetailServiceIMPL.getCurrentUser();
        comic.setName(comicDTO.getName());
        comic.setComic(comicDTO.getComic());
        comic.setUser(currentUser);
        Long idCategory = comicDTO.getIdCategory();
        Optional<Category> category = categoryService.findById(idCategory);
        if (!category.isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("category not found"), HttpStatus.NOT_FOUND);

        } else {
            comic.setCategory(category.get());
        }
        comicService.save(comic);
        return new ResponseEntity<>(new ResponseMessage("create success"), HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editSong(@PathVariable Long id, @RequestBody ComicDTO comicDTO) {
        Optional<Comic> comicOptional = comicService.findById(id);
        if (!comicOptional.isPresent()) {
            return new ResponseEntity<>(new ResponseMessage(" not found!"), HttpStatus.NOT_FOUND);
        }
        Comic comic = new Comic();
        User currentUser = userDetailServiceIMPL.getCurrentUser();
        comic.setId(id);
        comic.setName(comicDTO.getName());
        comic.setComic(comicDTO.getComic());
        Long idCategory = comicDTO.getIdCategory();
        comic.setUser(currentUser);
        Optional<Category> category = categoryService.findById(idCategory);
        if (!category.isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("category not found"), HttpStatus.NOT_FOUND);

        } else {
            comic.setCategory(category.get());
        }
        comicService.save(comic);
        return new ResponseEntity<>(new ResponseMessage("edit success"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detailComic(@PathVariable Long id) {
        Optional<Comic> comic = comicService.findById(id);
        if (!comic.isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("not found"), HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(comic.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Optional<Comic> comic) {
        if (!comic.isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("Song not found"), HttpStatus.NOT_FOUND);
        }
        comicService.deleteById(comic.get().getId());
        return new ResponseEntity<>(new ResponseMessage("Delete success!!"), HttpStatus.OK);
    }

    @GetMapping("/searchByName/{name}")
    public ResponseEntity<?> searchByName(@PathVariable String name) {
        return new ResponseEntity<>(comicService.findByNameContaining(name), HttpStatus.OK);
    }

    @GetMapping("/search/page")
    public ResponseEntity<?> searchPageSong(@RequestParam String name, Pageable pageable) {
        return new ResponseEntity<>(comicService.findByNameContaining(name, pageable), HttpStatus.OK);
    }

    @GetMapping("/searchByCategory/{name}")
    public ResponseEntity<?> searchByCategory(@PathVariable("name") String name) {
        return new ResponseEntity<>(categoryService.findByNameContaining(name), HttpStatus.OK);
    }
}
