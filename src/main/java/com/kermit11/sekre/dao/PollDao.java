package com.kermit11.sekre.dao;

import com.kermit11.sekre.model.Poll;

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

}
