package com.kermit11.sekre.model;

import java.util.UUID;

public class UserVotes
{
    private final String userID;
    private final UUID pollID;
    private boolean liked;
    private boolean votedFor;
    private boolean votedAgainst;

    public UserVotes(String userID, UUID pollID, boolean liked, boolean votedFor, boolean votedAgainst) {
        this.userID = userID;
        this.pollID = pollID;
        this.liked = liked;
        this.votedFor = votedFor;
        this.votedAgainst = votedAgainst;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isVotedFor() {
        return votedFor;
    }

    public void setVotedFor(boolean votedFor) {
        this.votedFor = votedFor;
    }

    public boolean isVotedAgainst() {
        return votedAgainst;
    }

    public void setVotedAgainst(boolean votedAgainst) {
        this.votedAgainst = votedAgainst;
    }
}
