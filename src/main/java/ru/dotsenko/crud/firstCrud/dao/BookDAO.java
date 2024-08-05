package ru.dotsenko.crud.firstCrud.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.dotsenko.crud.firstCrud.models.Book;
import ru.dotsenko.crud.firstCrud.models.Person;


import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Работает
    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM  Book", new BeanPropertyRowMapper<>(Book.class));
    }
    //Не работает
    public Book show(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }
    //Работает
    public void save(Book Book) {
        jdbcTemplate.update("INSERT INTO Book(title, author, year) VALUES(?, ?, ?)", Book.getTitle(), Book.getAuthor(),
                Book.getYear());
    }
    //Не работает
    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("UPDATE Book SET title=?, author=?, year=? WHERE id=?", updatedBook.getTitle(),
                updatedBook.getAuthor(), updatedBook.getYear(), id);
    }
    // ?
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
    }


    public Optional<Person> getBookOwner(int id) {
        return jdbcTemplate.query("SELECT person.* FROM person JOIN book ON person.id = person_id " +
                "WHERE book.id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }
    public void release(int id) {
        jdbcTemplate.update("UPDATE Book SET person_id=NULL WHERE id=?", id);
    }
    public void assign(int id, Person selectedPerson) {
        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE id=?", selectedPerson.getId(), id);
    }
}