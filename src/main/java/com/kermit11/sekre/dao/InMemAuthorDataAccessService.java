package com.kermit11.sekre.dao;

import com.kermit11.sekre.model.Author;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository("inMemAuthorRepo")
public class InMemAuthorDataAccessService implements AuthorDao
{
    private static final List<Author> allAuthors = new ArrayList<>();

    @Override
    public int insertAuthor(UUID id, Author author)
    {
        //TODO check if user already exists first
        allAuthors.add(new Author(id, author.getName()));
        return 1;
    }

    @Override
    public List<Author> searchAuthorByName(String partialName)
    {
        return allAuthors.stream()
                .filter(author -> author.getName().toUpperCase().contains(partialName.toUpperCase()))
                .collect(Collectors.toList());
    }


}
