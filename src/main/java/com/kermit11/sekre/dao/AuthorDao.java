package com.kermit11.sekre.dao;

import com.kermit11.sekre.model.Author;

import java.util.List;
import java.util.UUID;

public interface AuthorDao
{
    int insertAuthor(UUID id, Author author);
    default int insertAuthor(Author author)
    {
        UUID id = UUID.randomUUID();
        return insertAuthor(id, author);
    }

    List<Author> searchAuthorByName(String partialName);

}
