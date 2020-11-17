package com.kermit11.sekre.service;

import com.kermit11.sekre.config.LogicConfigProps;
import com.kermit11.sekre.controller.PaginationInfo;
import com.kermit11.sekre.dao.PollDao;
import com.kermit11.sekre.model.Author;
import com.kermit11.sekre.model.Poll;
import com.kermit11.sekre.model.VoteTotals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class PollService
{
    private final PollDao pollDao;
    private final VotingService votingService;
    private final LogicConfigProps logicProps;
    private static final Random randomGenerator = new Random();

    @Autowired
    public PollService(@Qualifier("sqlPollRepo") PollDao pollDao, VotingService votingService, LogicConfigProps logicProps) {
        this.pollDao = pollDao;
        this.votingService = votingService;
        this.logicProps = logicProps;
    }

    public int addPoll(Poll poll) {
        return pollDao.insertPoll(poll);
    }

    public int updatePoll(Poll poll)
    {
       return pollDao.updatePoll(poll);
    }

    public Poll getPollByID(UUID id)
    {
        Optional<Poll> pollOpt = Optional.ofNullable(pollDao.getPollById(id));
        pollOpt.ifPresent(this::updateUserVotes);

        return pollOpt.orElse(null);
    }

    /**
     * This actually returns a pseudo-random poll. We favor (already) popular polls.
     */
    public Poll getRandomPoll()
    {
        Optional<Poll> pollOpt = Optional.empty();

        boolean getWithLikes = decideIfFavorPollWithLikes();

        if (getWithLikes)
        {
            pollOpt = Optional.ofNullable(pollDao.getRandomPollWithLikes());
        }
        //else, or if previous condition was fulfilled but no such polls were found
        if(!getWithLikes || !pollOpt.isPresent())
        {
            pollOpt = Optional.ofNullable(pollDao.getRandomPoll());
        }

        pollOpt.ifPresent(this::updateUserVotes);

        return pollOpt.orElse(null);
    }

    protected boolean decideIfFavorPollWithLikes()
    {
        return randomGenerator.nextDouble() > logicProps.getProbabilityForPollWithNoLikes();
    }

    public List<Poll> getMostPopularPolls(PaginationInfo paginationInfo)
    {
        List<Poll> polls = pollDao.getPolls(PollDao.POLL_LIST_SORTING_TYPE.MOST_LIKES, PollDao.POLL_LIST_FILTER.NO_FILTER, null, paginationInfo);
        polls.forEach(this::updateUserVotes);

        return polls;
    }

    public List<Poll> getPollsByAuthor(Author author, PaginationInfo paginationInfo)
    {
        List<Poll> polls = pollDao.getPolls(PollDao.POLL_LIST_SORTING_TYPE.DEFAULT, PollDao.POLL_LIST_FILTER.AUTHOR, author, paginationInfo);
        polls.forEach(this::updateUserVotes);

        return polls;
    }

    public List<Poll> getOnAirPolls(PaginationInfo paginationInfo)
    {
        List<Poll> polls = pollDao.getPolls(PollDao.POLL_LIST_SORTING_TYPE.PUBLICATION_DATE, PollDao.POLL_LIST_FILTER.BROADCAST, Boolean.TRUE, paginationInfo);
        polls.forEach(this::updateUserVotes);

        return polls;
    }

    public List<Poll> searchPolls(String searchFor, PaginationInfo paginationInfo)
    {
        List<Poll> polls = pollDao.getPolls(PollDao.POLL_LIST_SORTING_TYPE.DEFAULT, PollDao.POLL_LIST_FILTER.SEARCH, searchFor, paginationInfo);
        polls.forEach(this::updateUserVotes);

        return polls;
    }

    public int getPollCount()
    {
        return pollDao.getPollCount();
    }


    private void updateUserVotes(Poll poll)
    {
        VoteTotals votes = votingService.getTotalVotes(poll.getId());
        poll.setVoteTotals(votes);
    }
}
