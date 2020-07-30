package com.kermit11.sekre.service;

import com.kermit11.sekre.dao.AuthorDao;
import com.kermit11.sekre.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuthorService
{
    private final AuthorDao authorDao;

    @Autowired
    public AuthorService(@Qualifier("inMemAuthorRepo") AuthorDao authorDao)
    {
        this.authorDao = authorDao;
    }

    public int addAuthor(Author author)
    {
        return authorDao.insertAuthor(author);
    }

    public Author createAuthor(String authorName)
    {
        Author author = new Author(UUID.randomUUID(), authorName);
        authorDao.insertAuthor(author);

        return author;
    }

    public List<Author> searchAuthorByName(String partialName)
    {
        return authorDao.searchAuthorByName(partialName);
    }
}
