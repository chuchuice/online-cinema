package edu.chuice.cinema.repositories;

import edu.chuice.cinema.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    List<Artist> findByName(final String name);
    List<Artist> findByNameAndSurname(final String name, final String surname);
    Optional<Artist> findById(final long id);
}