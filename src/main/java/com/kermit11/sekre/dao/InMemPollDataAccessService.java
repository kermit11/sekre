package com.kermit11.sekre.dao;

import com.kermit11.sekre.model.Poll;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("inMemPollRepo")
public class InMemPollDataAccessService implements PollDao
{
    private static final HashMap<UUID, Poll> allPolls = new HashMap<>();
    private static final Random randomGenerator = new Random();

    @Override
    public int insertPoll(UUID id, Poll poll)
    {
        allPolls.put(id, poll);
        return 1;
    }

    @Override
    public Poll getPollById(UUID id) {
        return allPolls.get(id);
    }

    @Override
    public Poll getRandomPoll()
    {
        Collection<Poll> polls = allPolls.values();
        int totalPolls = polls.size();
         if (totalPolls == 0) return null;
        int randIndex = randomGenerator.nextInt(totalPolls);

        return polls.stream()
                .skip(randIndex)
                .findFirst()
                .get();
    }

    @Override
    public int updatePoll(Poll poll)
    {
        allPolls.put(poll.getId(), poll);
        return 1;
    }
}
