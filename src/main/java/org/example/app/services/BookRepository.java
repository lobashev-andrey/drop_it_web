package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private ApplicationContext context;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Book> retrieveAll() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new ArrayList<>(books);
    }


    @Override
    public void store(Book book) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("size", book.getSize());
        jdbcTemplate.update("INSERT INTO books(author, title, size) VALUES(:author, :title, :size)", parameterSource);
        logger.info("store new book: " + book);
//        repo.add(book);

    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookIdToRemove);
        int count = jdbcTemplate.update("DELETE FROM books WHERE id = :id", parameterSource);
        if(count > 0){
            logger.info("remove book completed");
            return true;
        }
        return false;
    }

    @Override
    public boolean removeItemByRegex(String queryRegex) {


        String booksWithRegex = "";
        int removedBooksNumber = 0;
        for(Book book: retrieveAll()){
            if (book.getAuthor().contains(queryRegex)
                    || book.getTitle().contains(queryRegex)
                    || String.valueOf(book.getSize()).equals(queryRegex)) {
                booksWithRegex += ("\n\t" + book);
                removedBooksNumber++;
                removeItemById(book.getId());
            }
        }
        if(removedBooksNumber == 0) return false;

        logger.info("removed books: " + booksWithRegex +
                "\n\ttotal: " + removedBooksNumber);
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public void defaultInit() {
        logger.info("default INIT in BookRepository");
    }

    public void defaultDestroy() {
        logger.info("default DESTROY in BookRepository");
    }


}
