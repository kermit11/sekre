package com.kermit11.sekre.dao;

import com.kermit11.sekre.controller.PaginationInfo;
import com.kermit11.sekre.model.Author;
import com.kermit11.sekre.model.Poll;

import java.util.List;
import java.util.UUID;

public interface PollDao
{
    int insertPoll(UUID id, Poll poll);
    default int insertPoll (Poll poll)
    {
        UUID id = UUID.randomUUID();
        poll.setId(id);
        return insertPoll(id, poll);
    }

    Poll getPollById(UUID id);

    Poll getRandomPoll();

    int updatePoll(Poll poll);

    List<Poll> getTopPolls(POLL_LIST_SORTING_TYPE sortingType, int pageStart, int pageSize);

    List<Poll> getPollsByCriteria(POLL_LIST_FILTER filter, Object filterValue, PaginationInfo paginationInfo);

    int getPollCount();

    public enum POLL_LIST_SORTING_TYPE {MOST_LIKES, PUBLICATION_DATE}

    public enum POLL_LIST_FILTER {AUTHOR, BROADCAST}
}
