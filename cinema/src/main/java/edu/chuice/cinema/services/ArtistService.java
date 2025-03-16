package edu.chuice.cinema.services;

import edu.chuice.cinema.models.Artist;
import edu.chuice.cinema.repositories.ArtistRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ArtistService {
    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> findByName(@NotNull final String name) {
        return artistRepository.findByName(name);
    }

    public List<Artist> findByNameAndSurname(@NotNull final String name, @NotNull final String surname) {
        return artistRepository.findByNameAndSurname(name, surname);
    }

    @Cacheable(value = "artists")
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    @Cacheable(value = "artist")
    public Optional<Artist> findById(final long id) {
        return artistRepository.findById(id);
    }

    @Transactional(readOnly = false)
    @CachePut(value = "artists")
    public Artist save(@NotNull final Artist artist) {
        return artistRepository.save(artist);
    }

    @Transactional(readOnly = false)
    @CacheEvict(value = "artists", key = "#id")
    public Artist delete(final long id) {
        return artistRepository
                .findById(id)
                .map(artist -> {
                    artistRepository.delete(artist);
                    return artist;
                })
                .orElse(null);
    }
}