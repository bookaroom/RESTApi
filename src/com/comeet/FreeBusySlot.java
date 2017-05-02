package com.comeet;

/**
 * Class to store a room's free/busy information.
 */
public class FreeBusySlot {
    private String start;
    private String end;
    private String status;
    
    /**Default constructor for the class.
     * 
     * @param start start time of the slot
     * @param end end time of the slot
     * @param status status of the time
     */
    FreeBusySlot(String start, String end, String status) {
        this.start = start;
        this.end = end;
        this.status = status;
    }
    
    /**
     * Get the start of the time slot.
     * @return String 
     */
    public String getStart() {
        return start;
    }
    
    /**
     * Get the end of the time slot.
     * @return String 
     */
    public String getEnd() {
        return end;
    }

    /**
     * Get the status of the time slot.
     * @return String 
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Set the start time of the time slot.
     * @param start start time of the slot  
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * Set the end time of the time slot.
     * @param end end time of the slot  
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * Set the status of the time slot.
     * @param status status of the time slot  
     */    
    public void setStatus(String status) {
        this.status = status;
    }
}
