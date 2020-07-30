package com.kermit11.sekre.model;

import org.springframework.stereotype.Component;

@Component("voteTotals")
public class VoteTotals
{
    private int forVotes;
    private int againstVotes;
    private int likes;

    public VoteTotals(int forVotes, int againstVotes, int likes) {
        this.forVotes = forVotes;
        this.againstVotes = againstVotes;
        this.likes = likes;
    }

    public VoteTotals()
    {
        this.forVotes = 0;
        this.againstVotes = 0;
        this.likes = 0;
    }

    public int getForVotes() {
        return forVotes;
    }

    public int getAgainstVotes() {
        return againstVotes;
    }

    public int getLikes() {
        return likes;
    }

    public void incForVotes() {
        forVotes++;
    }

    public void incAgainstVotes(){
        againstVotes++;
    }

    public void incLikes()
    {
        likes++;
    }
}
