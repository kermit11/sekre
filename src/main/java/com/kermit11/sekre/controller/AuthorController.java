package com.kermit11.sekre.controller;

import com.kermit11.sekre.model.Author;
import com.kermit11.sekre.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/author")
@RestController
public class AuthorController
{
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService)
    {
        this.authorService = authorService;
    }

    //TODO: Check if this is really required. If it will be called then consider calling createAuthor() to avoid duplicates
    @PostMapping
    public void addAuthor(@RequestBody @NonNull Author author)
    {
        authorService.addAuthor(author);
    }

    @GetMapping(path = "{partialName}")
    public List<Author> searchAuthorByName(@PathVariable String partialName)
    {
        return authorService.searchAuthorByName(partialName);
    }

}
