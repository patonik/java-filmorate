package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.InvalidArgumentsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component("filmDbStorage")
public class FilmDbStorage extends DbStorage<Film> implements FilmStorage {
    private static final String INSERT =
        "INSERT INTO PUBLIC.FILM (NAME, " +
            "DESCRIPTION, " +
            "RELEASE_DATE, " +
            "DURATION, " +
            "MPA_CODE) " +
            "VALUES(?, ?, ?, ?, ?);";
    private static final String ADD_FILM_TO_GENRES =
        "INSERT INTO PUBLIC.GENRE_FILM (GENRE_ID, " +
            "FILM_ID) " +
            "VALUES(?, ?);";
    private static final String REMOVE_FILM_FROM_GENRES =
        "DELETE FROM PUBLIC.GENRE_FILM " +
            "WHERE FILM_ID = ?;";
    private static final String SELECT_FILM_GENRES = "SELECT gf.GENRE_ID ID, g.NAME FROM GENRE_FILM gf " +
        "LEFT JOIN GENRE g ON gf.GENRE_ID = g.ID " +
        "WHERE gf.FILM_ID = ?;";
    private static final String DELETE = "DELETE FROM PUBLIC.FILM WHERE ID = ?;";
    private static final String UPDATE =
        "UPDATE PUBLIC.FILM SET NAME = ?, " +
            "DESCRIPTION = ?, " +
            "RELEASE_DATE = ?, " +
            "DURATION = ?, " +
            "MPA_CODE = ? " +
            "WHERE ID = ?;";
    private static final String SELECT_ALL =
        "SELECT f.ID, " +
            "f.NAME, " +
            "f.DESCRIPTION, " +
            "f.RELEASE_DATE, " +
            "f.DURATION, " +
            "f.MPA_CODE mpaid, " +
            "mc.NAME mpacode " +
            "FROM FILM f " +
            "LEFT JOIN MPA_CODE mc ON f.MPA_CODE = mc.ID;";
    private static final String SELECT_ALL_GENRE = "SELECT * FROM PUBLIC.GENRE ORDER BY ID;";
    private static final String SELECT_ALL_MPA = "SELECT * FROM PUBLIC.MPA_CODE ORDER BY ID;";
    private static final String SELECT_ONE =
        "SELECT f.ID, " +
            "f.NAME, " +
            "f.DESCRIPTION, " +
            "f.RELEASE_DATE, " +
            "f.DURATION, " +
            "f.MPA_CODE mpaid, " +
            "mc.NAME mpacode " +
            "FROM FILM f " +
            "LEFT JOIN MPA_CODE mc ON f.MPA_CODE = mc.ID " +
            "WHERE f.ID = ?;";
    private static final String SELECT_ONE_GENRE = "SELECT * FROM PUBLIC.GENRE WHERE ID = ?;";
    private static final String SELECT_ONE_MPA = "SELECT * FROM PUBLIC.MPA_CODE WHERE ID = ?;";
    private static final String INSERT_LIKES = "INSERT INTO PUBLIC.USER_FAVE_FILM (FILM, \"user\") VALUES(?, ?);";
    private static final String DELETE_LIKES = "DELETE FROM PUBLIC.USER_FAVE_FILM WHERE FILM = ? AND \"user\" = ?;";
    private static final String SELECT_POPULAR =
        "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_CODE mpaid, mc.NAME mpacode FROM PUBLIC.FILM f " +
            "RIGHT JOIN (SELECT count(\"user\") AS c, FILM FROM PUBLIC.USER_FAVE_FILM GROUP BY FILM ORDER BY c DESC LIMIT ?) AS l ON f.ID = l.FILM " +
            "LEFT JOIN MPA_CODE mc ON f.MPA_CODE = mc.ID;";
    private final RowMapper<Genre> genreRowMapper;
    private final RowMapper<Mpa> mpaRowMapper;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper, RowMapper<Genre> genreRowMapper,
                         RowMapper<Mpa> mpaRowMapper) {
        super(jdbc, mapper);
        this.genreRowMapper = genreRowMapper;
        this.mpaRowMapper = mpaRowMapper;
    }

    public int insertLike(int filmId, int userId) {
        return jdbc.update(INSERT_LIKES, filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        jdbc.update(DELETE_LIKES, filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return findMany(SELECT_POPULAR, count);
    }

    @Transactional
    @Override
    public Film create(Film film) {
        int id = insert(INSERT,
            film.getName(),
            film.getDescription(),
            film.getReleaseDate(),
            film.getDuration(),
            film.getMpa().getId());
        film.setId(id);
        addGenres(id, film.getGenres());
        return film;
    }

    private void addGenres(int id, Set<Genre> genres) {
        for (Genre genre : genres) {
            try {
                jdbc.update(ADD_FILM_TO_GENRES, genre.getId(), id);
            } catch (DataAccessException e) {
                throw new InvalidArgumentsException(e.getMessage());
            }
        }
    }

    private void removeGenres(int id) {
        jdbc.update(REMOVE_FILM_FROM_GENRES, id);
    }

    @Override
    public Film delete(Film film) {
        if (delete(DELETE, film.getId())) {
            return film;
        }
        return null;
    }

    @Override
    public Film update(Film film) {
        int id = film.getId();
        update(UPDATE,
            film.getName(),
            film.getDescription(),
            film.getReleaseDate(),
            film.getDuration(),
            film.getMpa().getId(),
            id);
        removeGenres(id);
        addGenres(id, film.getGenres());
        return film;
    }

    @Override
    public List<Film> getAll() {
        return findMany(SELECT_ALL);
    }

    @Override
    public Film getById(int id) {
        Optional<Film> optionalFilm = findOne(SELECT_ONE, id);
        if (optionalFilm.isEmpty()) {
            return null;
        }
        Film film = optionalFilm.get();
        film.getGenres().addAll(jdbc.query(SELECT_FILM_GENRES, genreRowMapper, id));
        return film;
    }

    @Override
    public List<Genre> getGenres() {
        return jdbc.query(SELECT_ALL_GENRE, genreRowMapper);
    }

    @Override
    public List<Mpa> getMpas() {
        return jdbc.query(SELECT_ALL_MPA, mpaRowMapper);
    }

    @Override
    public Genre getGenreById(int id) {
        Genre genre = null;
        try {
            genre = jdbc.queryForObject(SELECT_ONE_GENRE, genreRowMapper, id);
        } catch (DataAccessException e) {
            throw new NotFoundException(e.getMessage());
        }
        return genre;
    }

    @Override
    public Mpa getMpaById(int id) {
        Mpa mpa = null;
        try {
            mpa = jdbc.queryForObject(SELECT_ONE_MPA, mpaRowMapper, id);
        } catch (DataAccessException e) {
            throw new NotFoundException(e.getMessage());
        }
        return mpa;
    }
}
