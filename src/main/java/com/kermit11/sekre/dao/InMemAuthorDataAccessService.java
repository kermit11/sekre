package com.kermit11.sekre.dao;

import com.kermit11.sekre.model.Author;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository("inMemAuthorRepo")
public class InMemAuthorDataAccessService implements AuthorDao
{
    private static final List<Author> allAuthors = new ArrayList<>();

    @Override
    public Author insertAuthor(UUID id, Author author)
    {
        allAuthors.add(author);
        return author;
    }

    @Override
    public List<Author> searchAuthorByName(String partialName)
    {
        return allAuthors.stream()
                .filter(author -> author.getName().toUpperCase().contains(partialName.toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Author> getAuthorByName(String name)
    {
        return allAuthors.stream()
                .filter(author -> author.getName().equals(name))
                .findAny();
    }
}
