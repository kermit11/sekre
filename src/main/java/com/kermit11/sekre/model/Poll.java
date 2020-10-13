package com.kermit11.sekre.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.UUID;

public class Poll
{
    private UUID id;
    @NonNull
    private String question;
    private Author author;
    private VoteTotals voteTotals;
    private Date publicationDate;
    private UUID origin;

    public Poll(@JsonProperty("question") String question, @JsonProperty("author") Author author, VoteTotals voteTotals, Date publicationDate, UUID origin)
    {
        this.question = question;
        this.author = author;
        this.voteTotals = (voteTotals !=null)? voteTotals :new VoteTotals();
        this.publicationDate = publicationDate;
        this.origin = origin;
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(@NonNull String question) {
        this.question = question;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public VoteTotals getVoteTotals() {
        return voteTotals;
    }

    public void setVoteTotals(VoteTotals voteTotals) {
        this.voteTotals = voteTotals;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public UUID getOrigin() {
        return origin;
    }

    public void setOrigin(UUID origin) {
        this.origin = origin;
    }


}
