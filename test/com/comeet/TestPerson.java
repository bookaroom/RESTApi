package com.comeet;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;


public class TestPerson {

    Person person = new Person();
    
    @Test
    public void test_NameGetter(){
        person.setName("Peter");
        assertEquals("Peter", person.getName());
    }
    
    
    @Test
    public void testEmailGetter(){
        person.setEmail("Me@meetl.ink");
        assertEquals("Me@meetl.ink",person.getEmail());
    }
}