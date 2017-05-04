
package com.comeet;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.comeet.BooleanResponse;

public class TestBooleanInput {

    BooleanResponse input = new BooleanResponse(true);
    
    @Test
    public void testGetSucces(){
        assertEquals(true, input.getSuccess());
    }
    
    @Test
    public void testSetSucces(){
       input.setSuccess(false);
       assertEquals(false, input.getSuccess());
    }
}