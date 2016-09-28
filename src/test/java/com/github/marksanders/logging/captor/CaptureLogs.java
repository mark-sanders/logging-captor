package com.github.marksanders.logging.captor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.rules.ExternalResource;

public class CaptureLogs extends ExternalResource {

    private final List<LoggingEvent> captured = Collections.synchronizedList(new ArrayList<LoggingEvent>());
    
    private class CaptureAppender extends AppenderSkeleton {

        @Override
        protected void append(LoggingEvent event) {
            captured.add(event);
        }

        public void close() {
            // nothing to do
        }

        public boolean requiresLayout() {
            return false;
        }
    }
    
    private CaptureAppender appender;

    @Override
    protected void before() throws Throwable {
        captured.clear();
        
        appender = new CaptureAppender();
        Logger.getRootLogger().addAppender(appender);
    }
    
    @Override
    protected void after() {
        Logger.getRootLogger().removeAppender(appender);
    }
    
    public List<LoggingEvent> getLoggingEvents() {
        return new ArrayList<LoggingEvent>(captured);
    }
    
    
    public static Predicate<? super LoggingEvent> classPredicate(final Class<?> klass) {
        return loggerNamePredicate(klass.getSimpleName());
    }

    public static Predicate<? super LoggingEvent> loggerNamePredicate(final String loggerName) {
        return loggingEvent -> loggingEvent.getLoggerName().contains(loggerName);
    }

    public static Predicate<? super LoggingEvent> levelPredicate(final Level level) {
        return loggingEvent -> loggingEvent.getLevel().isGreaterOrEqual(level);
    }

    public static Predicate<? super LoggingEvent> infoPredicate() {
        return levelPredicate(Level.INFO);
    }

    public static Predicate<? super LoggingEvent> warnPredicate() {
        return levelPredicate(Level.WARN);
    }
}
