package com.kermit11.sekre.controller;

import com.kermit11.sekre.model.Author;
import com.kermit11.sekre.model.Poll;
import com.kermit11.sekre.service.AuthorService;
import com.kermit11.sekre.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/poll")
@RestController
public class PollController {
    private final PollService pollService;
    private final AuthorService authorService;

    @Autowired
    public PollController(PollService pollService, AuthorService authorService) {
        this.pollService = pollService;
        this.authorService = authorService;
    }

    @PostMapping
    public void addPoll(@RequestBody Poll poll) {
        Author newAuthor = authorService.createAuthor(poll.getAuthor().getName());
        poll.setAuthor(newAuthor);
        pollService.addPoll(poll);
    }

    @GetMapping
    public Poll getRandomPoll() {
        return pollService.getRandomPoll();
    }

    @GetMapping(path = "{id}")
    public Poll getPollByID(@PathVariable("id") UUID id)
    {
        return pollService.getPollByID(id);
    }

    @GetMapping(path="popular")
    public List<Poll> getMostPopularPolls(@RequestParam int pageStart, @RequestParam int pageSize)
    {
        //TODO make this work like web controller
        return null;
        //return pollService.getMostPopularPolls(pageStart, pageSize);
    }
}
