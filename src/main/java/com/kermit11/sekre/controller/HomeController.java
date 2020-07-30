package com.kermit11.sekre.controller;

import com.kermit11.sekre.model.Author;
import com.kermit11.sekre.model.Poll;
import com.kermit11.sekre.model.UserVotes;
import com.kermit11.sekre.service.AuthorService;
import com.kermit11.sekre.service.PollService;
import com.kermit11.sekre.service.UserService;
import com.kermit11.sekre.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class HomeController {
    private final PollService pollService;
    private final AuthorService authorService;
    private final UserService userService;
    private final VotingService votingService;

    @Autowired
    public HomeController(PollService pollService, AuthorService authorService, UserService userService, VotingService votingService) {
        this.pollService = pollService;
        this.authorService = authorService;
        this.userService = userService;
        this.votingService = votingService;
    }

    @GetMapping("/")
    public String home(Model model) {
        Poll poll = pollService.getRandomPoll();
        if (poll == null) return "empty";

        String userDisplayName = userService.getCurrentUserName();
        String userID = userService.getCurrentUserEmail();
        UserVotes userVoting = votingService.getVotes(poll.getId(), userID);

        model.addAttribute("poll", poll);
        model.addAttribute("curUser", userDisplayName);
        model.addAttribute("userVoting", userVoting);

        return "index";
    }

    @RequestMapping(value = "/poll/{id}", method = RequestMethod.GET)
    public String getPollById(@PathVariable UUID id, Model model) {
        Poll poll = pollService.getPollByID(id);
        if (poll == null) return "empty";

        String userDisplayName = userService.getCurrentUserName();
        String userID = userService.getCurrentUserEmail();
        UserVotes userVoting = votingService.getVotes(poll.getId(), userID);

        model.addAttribute("poll", poll);
        model.addAttribute("curUser", userDisplayName);
        model.addAttribute("userVoting", userVoting);

        return "index";
    }

    @RequestMapping(value = "/vote", method = RequestMethod.POST, params = "voteType=voteFor")
    public String voteFor(@NonNull @ModelAttribute Poll poll) {
        String user = userService.getCurrentUserEmail();

        votingService.voteFor(poll.getId(), user);

        return "redirect:/poll/" + poll.getId();
    }

    @RequestMapping(value = "/vote", method = RequestMethod.POST, params = "voteType=voteAgainst")
    public String voteAgainst(@NonNull @ModelAttribute Poll poll) {
        String user = userService.getCurrentUserEmail();

        votingService.voteAgainst(poll.getId(), user);

        return "redirect:/poll/" + poll.getId();
    }

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public String toggleLike(@NonNull @ModelAttribute Poll poll)
    {
        String user = userService.getCurrentUserEmail();

        votingService.like(poll.getId(), user);

        return "redirect:/poll/" + poll.getId();
    }


    @GetMapping("/new")
    public String newPoll(Model model)
    {
        String user = userService.getCurrentUserName();

        model.addAttribute("question", new String());
        model.addAttribute("author", new String());
        model.addAttribute("curUser", user);

        return "newPoll";
    }

    @RequestMapping(value="/addPoll", method=RequestMethod.POST)
    public String addNewPoll(@RequestParam String question, @RequestParam String author)
    {
        Author newAuthor = authorService.createAuthor(author);
        Poll newPoll = new Poll(question, newAuthor, null, null);
        pollService.addPoll(newPoll);

        return "redirect:/poll/"+newPoll.getId();
    }

}
