package com.kermit11.sekre.dao;

import com.kermit11.sekre.model.UserVotes;
import com.kermit11.sekre.model.VoteTotals;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Repository("inMemUserVotesRepo")
public class InMemUserVotesDataAccessService implements UserVotesDao
{
    private static final HashMap<UUID, HashMap<String, UserVotes>> allVotes = new HashMap<>();

    @Override
    public int updateUserVotes( UUID pollID, String userID, UserVotes votes)
    {
        HashMap<String, UserVotes> pollVotes = allVotes.get(pollID);
        if (pollVotes == null)
        {
            pollVotes = new HashMap<>();
        }

        UserVotes userVotingData = new UserVotes(userID, pollID, votes.isLiked(), votes.isVotedFor(), votes.isVotedAgainst());
        pollVotes.put(userID, userVotingData);
        allVotes.put(pollID, pollVotes);

        return 1;
    }

    @Override
    public UserVotes getUserVotes(UUID pollID, String userID)
    {
        HashMap<String, UserVotes> pollVotes = allVotes.get(pollID);
        if (pollVotes == null) return new UserVotes(userID, pollID, false, false, false);

        return Optional.ofNullable(pollVotes.get(userID)).orElseGet(()->new UserVotes(userID, pollID, false, false, false));
    }

    @Override
    public VoteTotals getTotalVotes(UUID pollID)
    {
        HashMap<String, UserVotes> pollVotes = allVotes.get(pollID);
        if (pollVotes == null) return new VoteTotals(0, 0, 0);

        int forVotes = (int)pollVotes.values().stream()
                .filter(UserVotes::isVotedFor)
                .count();

        int againstVotes = (int)pollVotes.values().stream()
                .filter(UserVotes::isVotedAgainst)
                .count();

        int likes = (int)pollVotes.values().stream()
                .filter(UserVotes::isLiked)
                .count();

        VoteTotals totalVotes = new VoteTotals(forVotes, againstVotes, likes);

        return totalVotes;
    }
}
