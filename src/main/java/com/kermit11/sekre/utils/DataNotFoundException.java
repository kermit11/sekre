package com.kermit11.sekre.utils;

public class DataNotFoundException extends RuntimeException
{
    private String missingData;
    public String getMissingData() {
        return missingData;
    }

    public DataNotFoundException(String missingData)
    {
        this.missingData = missingData;
    }
}
