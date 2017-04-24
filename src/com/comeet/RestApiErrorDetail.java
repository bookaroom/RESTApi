package com.comeet;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.bind.annotation.XmlElement;

public class RestApiErrorDetail {

    private String domain;
    private Class<?> reason;
    private String message;
    private String trace;
    
    public RestApiErrorDetail(Throwable ex) {
        setReason(ex.getClass());
        setMessage(ex.getMessage());
        setTrace(getStackTraceFromThrowable(ex));
    }

    /**
     * The service identifier for this error.
     */
    public String getDomain() {
        return domain;
    }

    /**
     * The service identifier for this error.
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * The type string that caused this error.
     */
    public String getReason() {
        return trace.getClass().getSimpleName();
    }

    /**
     * The Java Throwable type that caused this error.
     */
    public void setReason(Class<?> reason) {
        this.reason = reason;
    }

    /**
     * Human-readable technical message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Human-readable technical message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Stack trace.
     */
    public String getTrace() {
        return trace;
    }

    /**
     * Stack trace.
     */
    @XmlElement
    public void setTrace(String trace) {
        this.trace = trace;
    }

    /**
     * Helper method to get the stack trace from a Java throwable.
     */
    private static String getStackTraceFromThrowable(Throwable ex) {
        // From http://stackoverflow.com/a/1149712
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
