package rikkei.academy.service.comic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rikkei.academy.model.Category;
import rikkei.academy.model.Comic;
import rikkei.academy.repository.IComicRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ComicServiceIMPL implements IComicService {
    @Autowired
    IComicRepository comicRepository;

    @Override
    public List<Comic> findAll() {
        return comicRepository.findAll();
    }

    @Override
    public Page<Comic> findAll(Pageable pageable) {
        return comicRepository.findAll(pageable);
    }

    @Override
    public Comic save(Comic comic) {
        return comicRepository.save(comic);
    }

    @Override
    public void deleteById(Long id) {
        comicRepository.deleteById(id);
    }

    @Override
    public Comic findById(Long id) {
        return comicRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean existsByName(String name) {
        return comicRepository.existsByName(name);
    }
}
