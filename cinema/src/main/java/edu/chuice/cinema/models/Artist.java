package edu.chuice.cinema.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "artist")
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Artist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "surname", nullable = false, length = 255)
    private String surname;

    @Column(name = "patronymic", nullable = true, length = 255)
    private String patronymic;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    public Artist(final String name, final String surname, final String patronymic, final LocalDate birthday) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.id = 0;
    }

}