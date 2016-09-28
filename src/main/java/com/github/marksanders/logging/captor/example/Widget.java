package com.github.marksanders.logging.captor.example;

import org.apache.log4j.Logger;

public class Widget {

    private static final Logger LOG= Logger.getLogger(Widget.class);

    private final String requiredString;
    private final int requiredPositiveNumber;

    public Widget(
            final String requiredString,
            final int requiredPositiveNumber,
            final Context context) {
        
        LOG.info("Creating a widget, context=[" + context + "]");

        if (requiredString == null || requiredString.isEmpty()) {
            final String message = 
                    "String must be supplied,"
                    + " requiredString=[" + requiredString  + "]"
                    + " context=[" + context + "]";
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }
        
        if (requiredPositiveNumber < 0) {
            final String message = 
                    "Number must be positive,"
                    + " requiredPositiveNumber=[" + requiredPositiveNumber  + "]"
                    + " context=[" + context + "]";
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }
        
        this.requiredString = requiredString;
        this.requiredPositiveNumber = requiredPositiveNumber;
    }
    
    public String getRequiredString() {
        return requiredString;
    }
    
    public int getRequiredPositiveNumber() {
        return requiredPositiveNumber;
    }
}
