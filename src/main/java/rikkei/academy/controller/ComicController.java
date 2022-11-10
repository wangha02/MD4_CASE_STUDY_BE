package rikkei.academy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rikkei.academy.dto.response.ResponseMessage;
import rikkei.academy.model.Comic;
import rikkei.academy.model.User;
import rikkei.academy.security.userprincipal.UserDetailServiceIMPL;
import rikkei.academy.service.comic.IComicService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/comic")
@CrossOrigin
public class ComicController {
    @Autowired
    private IComicService comicService;
    @Autowired
    private UserDetailServiceIMPL userDetailServiceIMPL;

    @GetMapping
    public ResponseEntity<?> showListComic(){
        Iterable<Comic> listComic = comicService.findAll();
        return new ResponseEntity<>(listComic, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createComic(@Valid @RequestBody Comic comic){
        User currentUser = userDetailServiceIMPL.getCurrentUser();
        if (comicService.existsByName(comic.getName())){
            return new ResponseEntity<>(new ResponseMessage("comic_invalid"),HttpStatus.OK);
        }
        comic.setUser(currentUser);
        comicService.save(comic);
        return new ResponseEntity<>(new ResponseMessage("create success"),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editSong(@PathVariable Long id, @RequestBody Comic comic) {
        Comic comic1 = comicService.findById(id);
//        if (comic1 == null){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
        comic1.setName(comic.getName());
        comic1.setComic(comic.getComic());
        comicService.save(comic1);
        return new ResponseEntity<>(new ResponseMessage(" edit success"),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Comic comic = comicService.findById(id);
        if (comic==null){
            return new ResponseEntity<>(new ResponseMessage("Comic does not exist"),HttpStatus.NOT_FOUND);
        }
        comicService.deleteById(id);
        return new ResponseEntity<>(new ResponseMessage("delete success"),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getComicById(@PathVariable Long id){
        return new ResponseEntity<>(comicService.findById(id),HttpStatus.OK);
    }

//    @GetMapping("/searchByName/{name}")
//    public ResponseEntity<?> searchByName(@PathVariable String name) {
//        return new ResponseEntity<>(comicService.findByNameContaining(name), HttpStatus.OK);
//    }
//
//    @GetMapping("/search/page")
//    public ResponseEntity<?> searchPageSong(@RequestParam String name, Pageable pageable) {
//        return new ResponseEntity<>(comicService.findByNameContaining(name, pageable), HttpStatus.OK);
//    }
//
//    @GetMapping("/searchByCategory/{name}")
//    public ResponseEntity<?> searchByCategory(@PathVariable("name") String name) {
//        return new ResponseEntity<>(categoryService.findByNameContaining(name), HttpStatus.OK);
//    }
}
