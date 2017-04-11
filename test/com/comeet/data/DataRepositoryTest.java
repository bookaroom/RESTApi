package com.comeet.data;

import junit.framework.Assert;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.comeet.Room;


@RunWith(MockitoJUnitRunner.class)
public class DataRepositoryTest {
    
    @InjectMocks 
    DataRepository dr;
    
    @Before
    public void setUp() {

    }
    
    //Tests for retrieveMetaData
    
    /**
     * check that empty string returns null object for retrieveRoomMetadata
     * @throws Exception
     */
    @Test
    public void retrieveMetaDataNullTest() throws Exception {
        Assert.assertSame(null, dr.retrieveRoomMetadata(""));
    }
    
    /**
     * check that class type of the return object on a successful call 
     * for retrieveRoomMetadata
     * @throws Exception
     */
    @Test
    public void retrieveMetaDataClassTest() throws Exception {
        Assert.assertSame(Room.class, dr.retrieveRoomMetadata("CambMa1Story305@meetl.ink").getClass());
    }
    
    /**
     * test for SQL Injection in the retrieveRoomMetadata method
     * @throws Exception
     */
    @Test
    public void retrieveMetaDataInjectionTest() throws Exception {
        Assert.assertSame(null, dr.retrieveRoomMetadata("select 3 from Rooms"));
    }
    
    /**
     * check for exception when null parameter passed in 
     * for retrieveRoomMetadata
     * @throws Exception
     */
    @Test
    public void retrieveMetaInputTest() throws Exception {
        Assert.assertSame(null, dr.retrieveRoomMetadata(null));
    }
    
    //Tests for retrieveSearchCriteria
    
    /**
     * check that empty string returns null object for retrieveSearchCriteria
     * @throws Exception
     */
    @Test
    public void retrieveSearchCriteriaNullTest() throws Exception {
        Assert.assertSame(null, dr.retrieveSearchCriteria(""));
    }
    
    /**
     * check that class type of the return object on a successful call 
     * for retrieveSearchCriteria
     * @throws Exception
     */
    @Test
    public void retrieveSearchCriteriaClassTest() throws Exception {
        Assert.assertSame(ArrayList.class, dr.retrieveSearchCriteria("meetl.ink")
                        .getClass());
    }
    
    /**
     * check for exception when null parameter passed in 
     * for retrieveSearchCriteria
     * @throws Exception
     */
    @Test
    public void retrieveSearchCriteriaInputTest() throws Exception {
        Assert.assertSame(null, dr.retrieveSearchCriteria(null));
    }
    
    /**
     * test for SQL Injection in the retrieveSearchCriteria method
     * @throws Exception
     */
    @Test
    public void retrieveSearchCriteriaInjectionTest() throws Exception {
        Assert.assertSame(null, dr.retrieveSearchCriteria("select 3 from Rooms"));
    }
}
