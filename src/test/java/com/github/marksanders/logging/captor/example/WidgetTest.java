package com.github.marksanders.logging.captor.example;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static com.github.marksanders.logging.captor.CaptureLogs.classPredicate;
import static com.github.marksanders.logging.captor.CaptureLogs.infoPredicate;
import static com.github.marksanders.logging.captor.CaptureLogs.warnPredicate;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.marksanders.logging.captor.CaptureLogs;

public class WidgetTest {
    
    private final Context context = new Context();
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Rule
    public CaptureLogs captureLogs = new CaptureLogs();

    @Test
    public void testConstructor() {
        
        Widget widget = new Widget("hello", 42, context);

        assertEquals("hello", widget.getRequiredString());
        assertEquals(42, widget.getRequiredPositiveNumber());

        List<String> logMessages = 
                captureLogs.getLoggingEvents().stream()
                    .filter(infoPredicate())
                    .filter(classPredicate(Widget.class))
                    .map(loggingEvent -> loggingEvent.getRenderedMessage())
                    .collect(toList());

        assertEquals(1, logMessages.size());
        
        String logMessage = logMessages.get(0);
        assertEquals("Creating a widget, context=[" + context + "]", logMessage);
    }

    @Test
    public void handleMissingRequiredString() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("String must be supplied");
        thrown.expectMessage(" requiredString=[null]");
        thrown.expectMessage(" context=[" + context + "]");
        
        new Widget(null, 42, context);
    }
    
    @Test
    public void handleEmptyRequiredString() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("String must be supplied");
        thrown.expectMessage(" requiredString=[]");
        thrown.expectMessage(" context=[" + context + "]");
        
        new Widget("", 42, context);
    }

    @Test
    public void handleNegativeNumber() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Number must be positive");
        thrown.expectMessage(" requiredPositiveNumber=[-42]");
        thrown.expectMessage(" context=[" + context + "]");
        
        new Widget("hello", -42, context);
    }
    

    @Test
    public void handleMissingRequiredStringLogged() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("String must be supplied");
        thrown.expectMessage(" requiredString=[null]");
        thrown.expectMessage(" context=[" + context + "]");
        
        try {
            new Widget(null, 42, context);
        } finally {
            checkWarnLogMessage(
                    "String must be supplied", 
                    " requiredString=[null]",
                    " context=[" + context + "]");
        }
    }
    
    @Test
    public void handleEmptyRequiredStringLogged() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("String must be supplied");
        thrown.expectMessage(" requiredString=[]");
        thrown.expectMessage(" context=[" + context + "]");
        
        try {
            new Widget("", 42, context);
        } finally {
            checkWarnLogMessage(
                    "String must be supplied", 
                    " requiredString=[]",
                    " context=[" + context + "]");
        }
    }

    @Test
    public void handleNegativeNumberLogged() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Number must be positive");
        thrown.expectMessage(" requiredPositiveNumber=[-42]");
        thrown.expectMessage(" context=[" + context + "]");
        
        try {
            new Widget("hello", -42, context);
        } finally {
            checkWarnLogMessage(
                    "Number must be positive", 
                    " requiredPositiveNumber=[-42]",
                    " context=[" + context + "]");
        }
    }

    private void checkWarnLogMessage(String ... substrings) {
        List<String> logMessages = 
                captureLogs.getLoggingEvents().stream()
                    .filter(warnPredicate())
                    .filter(classPredicate(Widget.class))
                    .map(loggingEvent -> loggingEvent.getRenderedMessage())
                    .collect(toList());

        assertEquals(1, logMessages.size());
        
        String logMessage = logMessages.get(0);
        for (String substring : substrings) {
            assertThat(logMessage, containsString(substring));
        }
    }
 }
