package com.comeet.data;

import com.comeet.Room;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;



@RunWith(MockitoJUnitRunner.class)
public class DataRepositoryTest {
    
    @InjectMocks 
    DataRepository dr;
    
    @Before
    public void setUp() {

    }
    
    //Tests for retrieveMetaData
    
    /**
     * Check that empty string returns null object for retrieveRoomMetadata.
     */
    @Test
    public void retrieveMetaDataNullTest() throws Exception {
        Assert.assertSame(null, dr.retrieveRoomMetadata(""));
    }
    
    /**
     * Check that class type of the return object on a successful call for retrieveRoomMetadata.
     */
    @Test
    public void retrieveMetaDataClassTest() throws Exception {
        Assert.assertSame(Room.class, dr.retrieveRoomMetadata("CambMa1Story305@meetl.ink").getClass());
    }
    
    /**
     * Test for SQL Injection in the retrieveRoomMetadata method.
     */
    @Test
    public void retrieveMetaDataInjectionTest() throws Exception {
        Assert.assertSame(null, dr.retrieveRoomMetadata("select 3 from Rooms"));
    }
    
    /**
     * Check for exception when null parameter passed in for retrieveRoomMetadata.
     */
    @Test
    public void retrieveMetaInputTest() throws Exception {
        Assert.assertSame(null, dr.retrieveRoomMetadata(null));
    }
    
    //Tests for retrieveSearchCriteria
    
    /**
     * check that empty string returns null object for retrieveSearchCriteria.
     */
    @Test
    public void retrieveSearchCriteriaNullTest() throws Exception {
        Assert.assertSame(null, dr.retrieveSearchCriteria(""));
    }
    
    /**
     * Check that class type of the return object on a successful call 
     * for retrieveSearchCriteria.
     */
    @Test
    public void retrieveSearchCriteriaClassTest() throws Exception {
        Assert.assertSame(ArrayList.class, dr.retrieveSearchCriteria("meetl.ink")
                        .getClass());
    }
    
    /**
     * Check for exception when null parameter passed in 
     * for retrieveSearchCriteria.
     */
    @Test
    public void retrieveSearchCriteriaInputTest() throws Exception {
        Assert.assertSame(null, dr.retrieveSearchCriteria(null));
    }
    
    /**
     * Test for SQL Injection in the retrieveSearchCriteria method.
     */
    @Test
    public void retrieveSearchCriteriaInjectionTest() throws Exception {
        Assert.assertSame(null, dr.retrieveSearchCriteria("select 3 from Rooms"));
    }
}
