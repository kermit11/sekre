package com.kermit11.sekre.dao;

import com.kermit11.sekre.model.Author;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorDao
{
    Author insertAuthor(UUID id, Author author);
    default Author insertAuthor(Author author)
    {
        UUID id = (author.getIndex()!=null)?author.getIndex():UUID.randomUUID();
        return insertAuthor(id, author);
    }

    List<Author> searchAuthorByName(String partialName);

    Optional<Author> getAuthorByName(String name);
}
