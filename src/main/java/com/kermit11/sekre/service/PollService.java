package com.kermit11.sekre.service;

import com.kermit11.sekre.dao.PollDao;
import com.kermit11.sekre.model.Poll;
import com.kermit11.sekre.model.VoteTotals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PollService
{
    private final PollDao pollDao;
    private final VotingService votingService;

    @Autowired
    public PollService(@Qualifier("inMemPollRepo") PollDao pollDao, VotingService votingService) {
        this.pollDao = pollDao;
        this.votingService = votingService;
    }

    public int addPoll(Poll poll) {
        return pollDao.insertPoll(poll);
    }

    public int updatePoll(Poll poll) {
       return pollDao.updatePoll(poll);
    }

    public Poll getPollByID(UUID id)
    {
        Optional<Poll> pollOpt = Optional.ofNullable(pollDao.getPollById(id));
        pollOpt.ifPresent(poll ->
        {
            VoteTotals votes = votingService.getTotalVotes(poll.getId());
            poll.setVoteTotals(votes);
        });

        return pollOpt.orElse(null);
    }

    public Poll getRandomPoll()
    {
        Optional<Poll> pollOpt = Optional.ofNullable(pollDao.getRandomPoll());
        pollOpt.ifPresent(poll ->
                {
                    VoteTotals votes = votingService.getTotalVotes(poll.getId());
                    poll.setVoteTotals(votes);
                });

        return pollOpt.orElse(null);
    }

    public List<Poll> getMostPopularPolls(int pageStart, int pageSize)
    {
        return pollDao.getTopPolls(PollDao.POLL_LIST_SORTING_TYPE.MOST_LIKES, pageStart, pageSize);
    }


}
