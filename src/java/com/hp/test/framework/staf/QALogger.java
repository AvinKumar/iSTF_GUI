
package com.hp.test.framework.staf;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Formatter;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;

/**
 * <code>QALogger</code> Our own version with a couple of extra methods to make life easy.
 * 
 * 
 */
public final class QALogger {

    private static final String FULL_QUALIFIED_CLASS_NAME = QALogger.class.getName();
    private final Logger wrapped;

    private static final int BANNER_WIDTH = 80;

    private QALogger(final Logger log4jLogger) {
        wrapped = log4jLogger;
    }

    /**
     * Get a logger by string. @see Logger#getLogger(String)
     * 
     * @param name
     * @return
     */
    public static synchronized QALogger getLogger(final String name) {
        return new QALogger(LogManager.getLogger(name));
    }

    /**
     * Get a logger by class. @see Logger#getLogger(Class)
     * 
     * @param className
     * @return
     */
    public static synchronized QALogger getLogger(final Class<?> className) {
        return new QALogger(LogManager.getLogger(className));
    }

    /**
     * get the root logger. @see Logger#getRootLogger()
     * 
     * @return
     */
    public static Logger getRootLogger() {
        return Logger.getRootLogger();
    }

    // ----------------------------------------------------------------------
    // Logging methods
    // ----------------------------------------------------------------------

    /**
     * Put out a trace message. @see Logger#trace(Object)
     * 
     * @param message
     */
    public void trace(final Object message) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.TRACE, message, null);
    }

    /**
     * put out a debug message. @see Category#debug(Object)
     * 
     * @param message
     */
    public void debug(final Object message) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.DEBUG, message, null);
    }

    /**
     * put out and info message. @see Category#info(Object)
     * 
     * @param message
     */
    public void info(final Object message) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.INFO, message, null);
    }

    /**
     * put out a warn message. @see Category#warn(Object)
     * 
     * @param message
     */
    public void warn(final Object message) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.WARN, message, null);
    }

    /**
     * put out an error message. @see Category#error(Object)
     * 
     * @param message
     */
    public void error(final Object message) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.ERROR, message, null);
    }

    /**
     * put out a fatal message. @see Category#fatal(Object)
     * 
     * @param message
     */
    public void fatal(final Object message) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.FATAL, message, null);
    }

    /**
     * put out a trace message. @see Logger#trace(Object,Throwable)
     * 
     * @param message
     * @param t
     */
    public void trace(final Object message, final Throwable t) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.TRACE, message, t);
    }

    /**
     * put out a debug message. @see Category#debug(Object,Throwable)
     * 
     * @param message
     * @param t
     */
    public void debug(final Object message, final Throwable t) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.DEBUG, message, t);
    }

    /**
     * put out an info message. @see Category#info(Object,Throwable)
     * 
     * @param message
     * @param t
     */
    public void info(final Object message, final Throwable t) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.INFO, message, t);
    }

    /**
     * put out a warn message. @see Category#warn(Object,Throwable)
     * 
     * @param message
     * @param t
     */
    public void warn(final Object message, final Throwable t) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.WARN, message, t);
    }

    /**
     * put out an error message. @see Category#error(Object,Throwable)
     * 
     * @param message
     * @param t
     */
    public void error(final Object message, final Throwable t) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.ERROR, message, t);
    }

    /**
     * put out a fatal message. @see Category#fatal(Object,Throwable)
     * 
     * @param message
     * @param t
     */
    public void fatal(final Object message, final Throwable t) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.FATAL, message, t);
    }

    /**
     * Log with supplied level. @see Logger#log(Priority,Object,Throwable)
     * 
     * @param level
     * @param message
     * @param t
     */
    public void log(final Priority level, final Object message, final Throwable t) {
        wrapped.log(level, message.toString(), t);
    }

    /**
     * Log with supplied level. @see Logger#log(Priority,Object)
     * 
     * @param level
     * @param message
     */
    public void log(final Priority level, final Object message) {
        wrapped.log(level, message);
    }

    /**
     * Log with supplied level. @see Logger#l7dlog(Priority,String,Throwable)
     * 
     * @param level
     * @param key
     * @param t
     */
    public void l7dlog(final Priority level, final String key, final Throwable t) {
        wrapped.l7dlog(level, key, t);
    }

    /**
     * Log with supplied level. @see Logger#l7dlog(Priority,String,Object[],Throwable)
     * 
     * @param level
     * @param key
     * @param params
     * @param t
     */
    public void l7dlog(
            final Priority level,
            final String key,
            final Object[] params,
            final Throwable t) {
        wrapped.l7dlog(level, java.text.MessageFormat.format(key, params), t);
    }

    // ----------------------------------------------------------------------
    // Non-logging methods of logger that just pass through to wrapped object
    // ----------------------------------------------------------------------

    /**
     * . @see Logger#addAppender(Appender)
     * 
     * @param newAppender
     */
    public void addAppender(final Appender newAppender) {
        wrapped.addAppender(newAppender);
    }

    /**
     * . @see Logger#callAppenders(LoggingEvent)
     * 
     * @param event
     */
    public void callAppenders(final LoggingEvent event) {
        wrapped.callAppenders(event);
    }

    /** . @see Logger#removeAllAppenders() */
    public void removeAllAppenders() {
        wrapped.removeAllAppenders();
    }

    /**
     * . @see Logger#removeAppender(Appender)
     * 
     * @param appender
     */
    public void removeAppender(final Appender appender) {
        wrapped.removeAppender(appender);
    }

    /**
     * . @see Logger#removeAppender(String)
     * 
     * @param name
     */
    public void removeAppender(final String name) {
        wrapped.removeAppender(name);
    }

    /**
     * . @see Logger#setAdditivity(boolean)
     * 
     * @param additive
     */
    public void setAdditivity(final boolean additive) {
        wrapped.setAdditivity(additive);
    }

    /**
     * . @see Logger#setLevel(Level)
     * 
     * @param level
     */
    public void setLevel(final Level level) {
        wrapped.setLevel(level);
    }

    /**
     * . @see Logger#getAdditivity()
     * 
     * @return
     */
    public boolean getAdditivity() {
        return wrapped.getAdditivity();
    }

    /**
     * . @see Logger#getAllAppenders()
     * 
     * @return
     */
    public Enumeration<?> getAllAppenders() {
        return wrapped.getAllAppenders();
    }

    /**
     * . @see Logger#getAppender(String )
     * 
     * @param name
     * @return
     */
    public Appender getAppender(final String name) {
        return wrapped.getAppender(name);
    }

    /**
     * . @see Logger#getEffectiveLevel()
     * 
     * @return
     */
    public Level getEffectiveLevel() {
        return wrapped.getEffectiveLevel();
    }

    /**
     * . @see Logger#getLoggerRepository()
     * 
     * @return
     */
    public LoggerRepository getLoggerRepository() {
        return wrapped.getLoggerRepository();
    }

    /**
     * . @see Logger#getParent()
     * 
     * @return
     */
    public Category getParent() {
        return wrapped.getParent();
    }

    /**
     * . @see Logger#isAttached(Appender)
     * 
     * @param appender
     * @return
     */
    public boolean isAttached(final Appender appender) {
        return wrapped.isAttached(appender);
    }

    /**
     * . @see Logger#isDebugEnabled()
     * 
     * @return
     */
    public boolean isDebugEnabled() {
        return wrapped.isDebugEnabled();
    }

    /**
     * . @see Logger#isEnabledFor(Priority)
     * 
     * @param level
     * @return
     */
    public boolean isEnabledFor(final Priority level) {
        return wrapped.isEnabledFor(level);
    }

    /**
     * . @see Logger#isInfoEnabled()
     * 
     * @return
     */
    public boolean isInfoEnabled() {
        return wrapped.isInfoEnabled();
    }

    /**
     * . @see Logger#isTraceEnabled()
     * 
     * @return
     */
    public boolean isTraceEnabled() {
        return wrapped.isTraceEnabled();
    }

    // ----------------------------------------------------------------------
    // Our custom logging methods.
    // ----------------------------------------------------------------------

    /**
     * Log a formated string with the INFO level.
     * 
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     */
    public void info(final String format, final Object... args) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.INFO, getFormatedMessaage(format, args), null);
    }

    /**
     * Log a formated string with the DEBUG level.
     * 
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     */
    public void debug(final String format, final Object... args) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.DEBUG, getFormatedMessaage(format, args), null);
    }

    /**
     * Log a formated string with the WARN level.
     * 
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     */
    public void warn(final String format, final Object... args) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.WARN, getFormatedMessaage(format, args), null);
    }

    /**
     * Log a formated string with the ERROR level.
     * 
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     */
    public void error(final String format, final Object... args) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.ERROR, getFormatedMessaage(format, args), null);
    }

    /**
     * Log a formated string with the FATAL level.
     * 
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     */
    public void fatal(final String format, final Object... args) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.FATAL, getFormatedMessaage(format, args), null);
    }

    /**
     * Log a formated string with the TRACE level.
     * 
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     */
    public void trace(final String format, final Object... args) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.TRACE, getFormatedMessaage(format, args), null);
    }

    /**
     * Log a message object with the INFO level including the stack trace of the Throwable t passed
     * as parameter.
     * 
     * @param t
     *            The throwable to get stack trace from.
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     **/
    public void info(final Throwable t, final String format, final Object... args) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.INFO, getFormatedMessaage(format, args), t);
    }

    /**
     * Log a message object with the DEBUG level including the stack trace of the Throwable t passed
     * as parameter.
     * 
     * @param t
     *            The throwable to get stack trace from.
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     **/
    public void debug(final Throwable t, final String format, final Object... args) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.DEBUG, getFormatedMessaage(format, args), t);
    }

    /**
     * Log a message object with the WARN level including the stack trace of the Throwable t passed
     * as parameter.
     * 
     * @param t
     *            The throwable to get stack trace from.
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     **/
    public void warn(final Throwable t, final String format, final Object... args) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.WARN, getFormatedMessaage(format, args), t);
    }

    /**
     * Log a message object with the ERROR level including the stack trace of the Throwable t passed
     * as parameter.
     * 
     * @param t
     *            The throwable to get stack trace from.
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     **/
    public void error(final Throwable t, final String format, final Object... args) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.ERROR, getFormatedMessaage(format, args), t);
    }

    /**
     * Log a message object with the FATAL level including the stack trace of the Throwable t passed
     * as parameter.
     * 
     * @param t
     *            The throwable to get stack trace from.
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     **/
    public void fatal(final Throwable t, final String format, final Object... args) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.FATAL, getFormatedMessaage(format, args), t);
    }

    /**
     * Log a message object with the TRACE level including the stack trace of the Throwable t passed
     * as parameter.
     * 
     * @param t
     *            The throwable to get stack trace from.
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     **/
    public void trace(final Throwable t, final String format, final Object... args) {
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.TRACE, getFormatedMessaage(format, args), t);
    }

    /**
     * @see java.util.Formatter#iformat by try catch block in case of throws.
     * 
     * @param format
     *            A format string as described in Format string syntax.
     * 
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     * @return A formated String.
     */
    private String getFormatedMessaage(final String format, final Object... args) {
        String msg;
        try {
            msg = new Formatter().format(format, args).toString();
        } catch (final Exception e) {
            msg = "format: "
                  + format
                  + " args: "
                  + Arrays.toString(args)
                  + " caused exception: "
                  + e.getMessage();
        }
        return msg;
    }

    /**
     * Log a message object with the INFO level including a banner wrapping the message.
     * 
     * @param bannerChar
     *            The character to make banner with.
     * @param message
     *            The message object to log.
     */
    public void infoBanner(final char bannerChar, final Object message) {
        final String bMessage = createBannerMessage(message.toString(), bannerChar);
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.INFO, bMessage, null);
    }

    /**
     * Log a message object with the DEBUG level including a banner wrapping the message.
     * 
     * @param bannerChar
     *            The character to make banner with.
     * @param message
     *            The message object to log.
     */
    public void debugBanner(final char bannerChar, final Object message) {
        final String bMessage = createBannerMessage(message.toString(), bannerChar);
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.DEBUG, bMessage, null);
    }

    /**
     * Log a message object with the WARN level including a banner wrapping the message.
     * 
     * @param bannerChar
     *            The character to make banner with.
     * @param message
     *            The message object to log.
     */
    public void warnBanner(final char bannerChar, final Object message) {
        final String bMessage = createBannerMessage(message.toString(), bannerChar);
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.WARN, bMessage, null);
    }

    /**
     * Log a message object with the ERROR level including a banner wrapping the message.
     * 
     * @param bannerChar
     *            The character to make banner with.
     * @param message
     *            The message object to log.
     */
    public void errorBanner(final char bannerChar, final Object message) {
        final String bMessage = createBannerMessage(message.toString(), bannerChar);
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.ERROR, bMessage, null);
    }

    /**
     * Log a message object with the FATAL level including a banner wrapping the message.
     * 
     * @param bannerChar
     *            The character to make banner with.
     * @param message
     *            The message object to log.
     */
    public void fatalBanner(final char bannerChar, final Object message) {
        final String bMessage = createBannerMessage(message.toString(), bannerChar);
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.FATAL, bMessage, null);
    }

    /**
     * Log a message object with the TRACE level including a banner wrapping the message.
     * 
     * @param bannerChar
     *            The character to make banner with.
     * @param message
     *            The message object to log.
     */
    public void traceBanner(final char bannerChar, final Object message) {
        final String bMessage = createBannerMessage(message.toString(), bannerChar);
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.TRACE, bMessage, null);
    }

    /**
     * Log a formated string with the INFO level including a banner wrapping the message.
     * 
     * @param bannerChar
     *            The character to make banner with.
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     */
    public void infoBanner(final char bannerChar, final String format, final Object... args) {
        final String bMessage = createBannerMessage(getFormatedMessaage(format, args), bannerChar);
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.INFO, bMessage, null);
    }

    /**
     * Log a formated string with the DEBUG level including a banner wrapping the message.
     * 
     * @param bannerChar
     *            The character to make banner with.
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     */
    public void debugBanner(final char bannerChar, final String format, final Object... args) {
        final String bMessage = createBannerMessage(getFormatedMessaage(format, args), bannerChar);
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.DEBUG, bMessage, null);
    }

    /**
     * Log a formated string with the WARN level including a banner wrapping the message.
     * 
     * @param bannerChar
     *            The character to make banner with.
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     */
    public void warnBanner(final char bannerChar, final String format, final Object... args) {
        final String bMessage = createBannerMessage(getFormatedMessaage(format, args), bannerChar);
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.WARN, bMessage, null);
    }

    /**
     * Log a formated string with the ERROR level including a banner wrapping the message.
     * 
     * @param bannerChar
     *            The character to make banner with.
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     */
    public void errorBanner(final char bannerChar, final String format, final Object... args) {
        final String bMessage = createBannerMessage(getFormatedMessaage(format, args), bannerChar);
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.ERROR, bMessage, null);
    }

    /**
     * Log a formated string with the FATAL level including a banner wrapping the message.
     * 
     * @param bannerChar
     *            The character to make banner with.
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     */
    public void fatalBanner(final char bannerChar, final String format, final Object... args) {
        final String bMessage = createBannerMessage(getFormatedMessaage(format, args), bannerChar);
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.FATAL, bMessage, null);
    }

    /**
     * Log a formated string with the TRACE level including a banner wrapping the message.
     * 
     * @param bannerChar
     *            The character to make banner with.
     * @param format
     *            A format string as described in Format string syntax.
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     */
    public void traceBanner(final char bannerChar, final String format, final Object... args) {
        final String bMessage = createBannerMessage(getFormatedMessaage(format, args), bannerChar);
        wrapped.log(FULL_QUALIFIED_CLASS_NAME, Level.TRACE, bMessage, null);
    }

    /**
     * Creates a bannerized log message.
     * 
     * @param message
     *            The desired log message.
     * @param bannerChar
     *            The character to make banner with.
     * @return The bannered massage.
     */
    private String createBannerMessage(final String message, final char bannerChar) {
        // 1. Create banner;
        final char[] buff = new char[BANNER_WIDTH];
        Arrays.fill(buff, 0, BANNER_WIDTH, bannerChar);
        final String banner = new String(buff);

        // 2. Format message;
        final StringBuilder buf = new StringBuilder();
        buf.append("\n");
        buf.append(banner);
        buf.append("\n");
        buf.append(banner);
        buf.append("\n");
        int lineSize = 0;
        for (final String word : message.split("\\s+")) {
            if ((lineSize + word.length()) >= BANNER_WIDTH) {
                buf.append("\n");
                lineSize = 0;
            } else {
                buf.append(" ");
            }
            buf.append(word);
            lineSize += word.length();
        }
        buf.append("\n");
        buf.append(banner);
        buf.append("\n");
        buf.append(banner);
        buf.append("\n");
        return buf.toString();
    }
}
