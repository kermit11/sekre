package com.kermit11.sekre.dao;

import com.kermit11.sekre.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("sqlAuthorRepo")
public class SQLAuthorDataAccessService implements AuthorDao
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Author insertAuthor(UUID id, Author author)
    {
        author.setIndex(id);

        String sqlStatement = "INSERT INTO authors (id, name) values (?, ?)";
        jdbcTemplate.update(sqlStatement, new Object[]{id.toString(), author.getName()});

        return author;

    }

    @Override
    public List<Author> searchAuthorByName(String partialName)
    {
        String sqlStatement = "SELECT id, name"
                + " FROM authors"
                + " WHERE name LIKE ?";
        String partialNameRegexParam = "%"+partialName+"%";

        List<Author> authors = jdbcTemplate.query(sqlStatement, new AuthorRowMapper(), partialNameRegexParam);

        return authors;
    }

    @Override
    public Optional<Author> getAuthorByName(String name)
    {
        String sqlStatement = "SELECT id, name"
                + " FROM authors"
                + " WHERE name = ?";

        Author author = DataAccessUtils.singleResult(jdbcTemplate.query(sqlStatement, new AuthorRowMapper(), name));

        return Optional.ofNullable(author);
    }


    private static class AuthorRowMapper implements RowMapper<Author>
    {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException
        {
            String authorName = resultSet.getString("name");
            UUID authorID = UUID.fromString(resultSet.getString("id"));
            Author author = new Author(authorID, authorName);

            return author;
        }
    }
}
