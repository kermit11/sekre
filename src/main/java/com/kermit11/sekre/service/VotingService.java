package com.kermit11.sekre.service;

import com.kermit11.sekre.dao.UserVotesDao;
import com.kermit11.sekre.model.UserVotes;
import com.kermit11.sekre.model.VoteTotals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VotingService {
    private final UserVotesDao userVotesDao;

    @Autowired
    public VotingService(@Qualifier("inMemUserVotesRepo") UserVotesDao userVotesDao) {
        this.userVotesDao = userVotesDao;
    }

    public UserVotes getVotes(UUID pollID, String userID) {
        return userVotesDao.getUserVotes(pollID, userID);
    }

    public VoteTotals getTotalVotes(UUID pollID)
    {
        return userVotesDao.getTotalVotes(pollID);
    }

    public int like(UUID pollID, String performer)
    {
        UserVotes currentVotes = getVotes(pollID, performer);
        currentVotes.setLiked(!currentVotes.isLiked());

        return userVotesDao.updateUserVotes(pollID, performer, currentVotes);
    }

    public int voteFor(UUID pollID, String performer)
    {
        UserVotes currentVotes = getVotes(pollID, performer);
        currentVotes.setVotedFor(true);
        //We do not allow both for and against
        currentVotes.setVotedAgainst(false);

        return userVotesDao.updateUserVotes(pollID, performer, currentVotes);
    }

    public int voteAgainst(UUID pollID, String performer)
    {
        UserVotes currentVotes = getVotes(pollID, performer);
        currentVotes.setVotedAgainst(true);
        //We do not allow both for and against
        currentVotes.setVotedFor(false);

        return userVotesDao.updateUserVotes(pollID, performer, currentVotes);
    }
}
