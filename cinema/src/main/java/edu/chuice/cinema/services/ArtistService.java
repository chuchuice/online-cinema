package edu.chuice.cinema.services;

import edu.chuice.cinema.models.Artist;
import edu.chuice.cinema.repositories.ArtistRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final CacheManager cacheManager;

    @Autowired
    public ArtistService(ArtistRepository artistRepository, CacheManager cacheManager) {
        this.artistRepository = artistRepository;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = "artists")
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    //TODO Поиск по кэшу не работает
    @Cacheable(value = "artist", key = "#id")
    public Optional<Artist> findById(final long id) {
        Cache cache = cacheManager.getCache("artists");

        if (cache == null) {
            return artistRepository.findById(id);
        }

        Map<Long, Optional<Artist>> artists = cache.get("artists", Map.class);

        Optional<Artist> artist = Optional.ofNullable(artists)
                .flatMap(map -> map.get(id));

        return artist.isPresent() ? artist : artistRepository.findById(id);
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