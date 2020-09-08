package com.kermit11.sekre.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import java.util.UUID;

public class Author
{
    private UUID index;
    @NonNull
    private final String name;

    public Author(UUID index, @JsonProperty("name") String name)
    {
        this.index = index;
        this.name = name;
    }


    public String getName()
    {
        return name;
    }

    public UUID getIndex() { return index; }
    public void setIndex(UUID index) {
        this.index = index;
    }
}
