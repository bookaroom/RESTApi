package com.comeet;

import static org.junit.Assert.*;

import org.junit.Test;

public class testExchangeConnection {

    private ExchangeConnection exCo = new ExchangeConnection();
    
    @Test
    public void test_ExchangeConncection(){
        assertNotNull(exCo);
    }

}
