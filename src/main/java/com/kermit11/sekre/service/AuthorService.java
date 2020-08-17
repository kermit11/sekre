package com.kermit11.sekre.service;

import com.kermit11.sekre.dao.AuthorDao;
import com.kermit11.sekre.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public Author addAuthor(Author author)
    {
        return authorDao.insertAuthor(author);
    }

    public Author createAuthor(String authorName)
    {
        //Don't create duplicates, work with existing object if possible
        Optional<Author> author = getAuthorByName(authorName);
        return author.orElseGet(() -> {
            Author newAuthor = new Author(UUID.randomUUID(), authorName);
            addAuthor(newAuthor);
            return newAuthor;
        });
    }

    public List<Author> searchAuthorByName(String partialName)
    {
        return authorDao.searchAuthorByName(partialName);
    }

    public Optional<Author> getAuthorByName(String name) { return authorDao.getAuthorByName(name);}
}
