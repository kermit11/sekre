package com.kermit11.sekre.controller;

import com.kermit11.sekre.model.Poll;
import com.kermit11.sekre.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/poll")
@RestController
public class PollController {
    private final PollService pollService;

    @Autowired
    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @PostMapping
    public void addPoll(@RequestBody Poll poll) {
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
        return pollService.getMostPopularPolls(pageStart, pageSize);
    }
}
