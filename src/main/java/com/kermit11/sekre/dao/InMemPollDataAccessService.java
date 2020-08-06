package com.kermit11.sekre.dao;

import com.kermit11.sekre.model.Poll;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository("inMemPollRepo")
public class InMemPollDataAccessService implements PollDao
{
    private static final HashMap<UUID, Poll> allPolls = new HashMap<>();
    private static final Random randomGenerator = new Random();
    private static final Map<POLL_LIST_SORTING_TYPE, Comparator<Poll>> sorters = Map.of
            (
                    POLL_LIST_SORTING_TYPE.MOST_LIKES,
                    new Comparator<Poll>() { public int compare(Poll poll1, Poll poll2) {
                        Integer p1Likes = poll1.getVoteTotals().getLikes();
                        Integer p2Likes = poll2.getVoteTotals().getLikes();
                        //Most likes == most to least, so reverse order
                        return p2Likes.compareTo(p1Likes);
                    }
            }
    );


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

    @Override
    public List<Poll> getTopPolls(POLL_LIST_SORTING_TYPE sortingType, int pageStart, int pageSize)
    {
        List<Poll> retPoles = allPolls.values().stream()
                .sorted(sorters.get(sortingType))
                .skip(pageStart)
                .limit(pageSize)
                .collect(Collectors.toList());
        return retPoles;
    }

    @Override
    public int getPollCount()
    {
        return allPolls.size();
    }
}
