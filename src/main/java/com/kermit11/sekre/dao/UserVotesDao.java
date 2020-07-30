package com.kermit11.sekre.dao;

import com.kermit11.sekre.model.UserVotes;
import com.kermit11.sekre.model.VoteTotals;

import java.util.UUID;

public interface UserVotesDao
{

    int updateUserVotes(UUID pollID, String userID, UserVotes votes);

    UserVotes getUserVotes(UUID pollID, String userID);

    VoteTotals getTotalVotes(UUID pollID);

}
