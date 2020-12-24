package com.kermit11.sekre.controller;

import com.kermit11.sekre.model.*;
import com.kermit11.sekre.service.AuthorService;
import com.kermit11.sekre.service.PollService;
import com.kermit11.sekre.service.UserService;
import com.kermit11.sekre.service.VotingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PollService pollService;

    @MockBean
    private UserService userService;

    @MockBean
    private VotingService votingService;

    @MockBean
    private AuthorService authorService;

    private final Author sampleAuthor = new Author(UUID.randomUUID(), "sample author");
    private final User sampleUser = new User("tester@kermit11.com", "tester", false);

    @BeforeEach
    public void setUp() {
        VoteTotals sampleVoteTotals = new VoteTotals();
        Poll samplePoll = new Poll("test question", sampleAuthor, sampleVoteTotals, null, null);
        samplePoll.setId(UUID.randomUUID());
        when(pollService.getRandomPoll()).thenReturn(samplePoll);
        when(pollService.getPollByID(any())).thenReturn(samplePoll);

        when(userService.getCurrent()).thenReturn(sampleUser);

        UserVotes sampleUserVotes = new UserVotes(sampleUser.getUserID(), samplePoll.getId(), false, false, false);
        when(votingService.getVotes(any(), any())).thenReturn(sampleUserVotes);

        when(authorService.createAuthor(any())).thenReturn(sampleAuthor);
    }

    @Test
    void home() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("curUser", "tester"))
                .andExpect(model().attribute("poll", hasProperty("question", equalTo("test question"))))
                .andExpect(model().attribute("isAdmin", Boolean.FALSE))
                .andExpect(view().name("index"));
    }

    @Test
    void homeNoPolls() throws Exception {
        //Override the setup mock with returning null
        when(pollService.getRandomPoll()).thenReturn(null);

        this.mockMvc.perform(get("/"))
                .andExpect(redirectedUrl("/new"));
    }

    @Test
    void getPollById() throws Exception {
        this.mockMvc.perform(get("/poll/" + UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("curUser", "tester"))
                .andExpect(model().attribute("poll", hasProperty("question", equalTo("test question"))))
                .andExpect(model().attribute("isAdmin", Boolean.FALSE))
                .andExpect(view().name("index"));
    }

    @Test
    void getPollByIdNoPolls() throws Exception {
        //Override the setup mock with returning null
        when(pollService.getPollByID(any())).thenReturn(null);

        this.mockMvc.perform(get("/poll/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(view().name("errorDataNotFound"));
    }

    @Test
    void login() throws Exception {
        this.mockMvc.perform(get("/login").header("Referer", "www.sekre.co.il/"))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("url_prior_login", "www.sekre.co.il/"))
                .andExpect(view().name("login"));
    }

    @Test
    void about() throws Exception {
        this.mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("curUser", "tester"))
                .andExpect(view().name("about"));
    }

    /**
     * This end point requires authorization, our only test is to see that the anonymous request is blocked
     */
    @Test
    void voteForRequiresLogin() throws Exception {
        this.mockMvc.perform(post("/vote")
                .param("id", UUID.randomUUID().toString())
                .param("voteType", "voteFor"))
                .andExpect(redirectedUrlPattern("http://*/login"));

    }

    @Test
    void newPoll() throws Exception {
        this.mockMvc.perform(get("/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("curUser", "tester"))
                .andExpect(model().attribute("isAdmin", false))
                .andExpect(view().name("newPoll"));

    }

    @Test
    void addNewPoll() throws Exception {
        when(pollService.addPoll(any())).thenReturn(anyInt());

        this.mockMvc.perform(post("/addPoll")
                .param("question", "testQuestion")
                .param("author", "testAuthor")
                .param("publicationDate", "2020-12-25"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/poll/*"));
    }

    @Test
    void markDuplicate() throws Exception
    {
        this.mockMvc.perform(post("/markDuplicate")
                .param("id", UUID.randomUUID().toString())
                .param("original", UUID.randomUUID().toString()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/poll/*"));
    }

    @Test
    void getMostPopular() throws Exception
    {
        when(pollService.getMostPopularPolls(any()))
                .thenReturn(new ArrayList<>());

        this.mockMvc.perform(get("/popular"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("listingTitle", "הכי אהובים"))
                .andExpect(model().attribute("curUser", "tester"))
                .andExpect(model().attributeExists("polls"))
                .andExpect(model().attribute("pagInfo", hasProperty("pageStart", equalTo(1))))
                .andExpect(view().name("listPolls"));
    }

    @Test
    void searchPage() throws Exception
    {
        this.mockMvc.perform(get("/search"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("curUser", "tester"))
                .andExpect(view().name("search"));
    }

    @Test
    void submitSearch() throws Exception
    {
        this.mockMvc.perform(post("/search")
                .param("searchTerm", "testSearch"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/search/testSearch"));
    }

    @Test
    void getSearchResults() throws Exception
    {
        this.mockMvc.perform(get("/search/testSearch"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("listingTitle", containsString("testSearch")))
                .andExpect(view().name("listPolls"));
    }

    @Test
    void getPollsByAuthor() throws Exception
    {
        when(authorService.getAuthorByName(any()))
                .thenReturn(Optional.of(sampleAuthor));

        this.mockMvc.perform(get("/author/testAuthor"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("listingTitle", containsString("testAuthor")))
                .andExpect(view().name("listPolls"));
    }

    @Test
    void getPollsByAuthorNoAuthor() throws Exception
    {
        when(authorService.getAuthorByName(any()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(get("/author/testAuthor"))
                .andExpect(status().isNotFound())
                .andExpect(model().attribute("missingData", "המחבר testAuthor"))
                .andExpect(view().name("errorDataNotFound"));
    }
}