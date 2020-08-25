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

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
        if (poll == null) return "redirect:/new";

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
        if (poll == null) return "redirect:/new";

        String userDisplayName = userService.getCurrentUserName();
        String userID = userService.getCurrentUserEmail();
        UserVotes userVoting = votingService.getVotes(poll.getId(), userID);

        model.addAttribute("poll", poll);
        model.addAttribute("curUser", userDisplayName);
        model.addAttribute("userVoting", userVoting);

        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("url_prior_login", referrer);

        return "login";
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
    public String toggleLike(@NonNull @ModelAttribute Poll poll) {
        String user = userService.getCurrentUserEmail();

        votingService.like(poll.getId(), user);

        return "redirect:/poll/" + poll.getId();
    }


    @GetMapping("/new")
    public String newPoll(Model model) {
        String user = userService.getCurrentUserName();

        model.addAttribute("question", "");
        model.addAttribute("author", "");
        model.addAttribute("curUser", user);

        return "newPoll";
    }

    @RequestMapping(value = "/addPoll", method = RequestMethod.POST)
    public String addNewPoll(@RequestParam String question, @RequestParam String author) {
        Author newAuthor = authorService.createAuthor(author);
        Poll newPoll = new Poll(question, newAuthor, null, null);
        pollService.addPoll(newPoll);

        return "redirect:/poll/" + newPoll.getId();
    }

    @RequestMapping(value = "/popular", method = RequestMethod.GET)
    public String getMostPopular(@RequestParam(required = false) Integer pageStart, @RequestParam(required = false) Integer pageSize, Model model)
    {
        populatePollList(pageStart, pageSize, pollService::getMostPopularPolls, model);

        //TODO: temp testing code, remove when done
        @SuppressWarnings("unchecked")
        List<Poll> topPolls = (List<Poll>) model.getAttribute("polls");
        if (topPolls != null && topPolls.size() >= 2) {
            topPolls.get(0).setPublicationDate(new Date());
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, 3);
            topPolls.get(1).setPublicationDate(cal.getTime());
        }

        model.addAttribute("listingTitle", "הכי אהובים");

        return "listPolls";
    }

    @RequestMapping(value = "/author/{name}", method = RequestMethod.GET)
    public String getPollsByAuthor(@PathVariable String name, @RequestParam(required = false) Integer pageStart, @RequestParam(required = false) Integer pageSize, Model model)
    {
        Author author = authorService.getAuthorByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Author " + name + " doesn't exist!"));

        populatePollList(pageStart, pageSize, pag -> pollService.getPollsByAuthor(author, pag), model);
        model.addAttribute("listingTitle", "הסקרים של: " + name);

        return "listPolls";
    }

    @RequestMapping(value = "/onair", method = RequestMethod.GET)
    public String getBroadcastPolls(@RequestParam(required = false) Integer pageStart, @RequestParam(required = false) Integer pageSize, Model model)
    {
        populatePollList(pageStart, pageSize, pollService::getOnAirPolls, model);
        model.addAttribute("listingTitle", "הסקרים שעלו לשידור");

        return "listPolls";
    }

    private void populatePollList(Integer pageStart, Integer pageSize, PollRetriever iPollRetriever, Model model)
    {
        if (pageStart == null) pageStart = 1;
        if (pageSize == null) pageSize = 10; //TODO toeknize

        String user = userService.getCurrentUserName();

        PaginationInfo pagInfo = new PaginationInfo(pageStart, pageSize, 0);

        //The invoker tells us which method to use for retrieving the polls using the input functional interface
        List<Poll> pollList = iPollRetriever.retrieve(pagInfo);

        if (pagInfo.getPageStart() > pagInfo.getTotalSize()) {
            //TODO: Err properly
            throw new IllegalArgumentException("pageStart value too high!");
        }

        model.addAttribute("curUser", user);
        model.addAttribute("polls", pollList);
        model.addAttribute("pagInfo", pagInfo);


    }

    @FunctionalInterface
    interface PollRetriever
    {
        List<Poll> retrieve(PaginationInfo paginationInfo);
    }

}
