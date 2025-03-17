package edu.chuice.cinema.controllers;

import edu.chuice.cinema.models.Artist;
import edu.chuice.cinema.services.ArtistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/artists")
public class ArtistController {

    private final ArtistService artistService;

    @Autowired()
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping()
    public ResponseEntity<List<Artist>> getAllArtists() {
        return ResponseEntity
                .ok()
                .body(this.artistService.findAll());

    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtist(@PathVariable("id") int id) {
        return artistService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<?> addArtist(@Valid @RequestBody Artist artist, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Ошибка валидации");
        }

        Artist newArtist = artistService.save(artist);

        return ResponseEntity
                .created(URI.create("/api/v1/artists/" + newArtist.getId()))
                .body(newArtist);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editArtist(@RequestBody Artist updated, @PathVariable("id") long id) {
        Artist artist = artistService.findById(id).orElse(null);

        if (artist == null) {
            return ResponseEntity.notFound().build();
        }

        {
            artist.setName(updated.getName() == null ? artist.getName() : updated.getName());
            artist.setSurname(updated.getSurname() == null ? artist.getSurname() : updated.getSurname());
            artist.setPatronymic(updated.getPatronymic() == null ? artist.getPatronymic() : updated.getPatronymic());
            artist.setBirthday(updated.getBirthday() == null ? artist.getBirthday() : updated.getBirthday());
        }

        return ResponseEntity.ok(artistService.save(artist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable("id") long id) {
        return artistService.delete(id) != null ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

}