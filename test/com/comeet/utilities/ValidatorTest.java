package com.comeet.utilities;

import org.junit.Assert;
import org.junit.Test;

public class ValidatorTest {
    
    /**
     * Validate email address test
     */
    @Test
    public void ValidateEmailTest()
    {
        //Valid email addresses
        Assert.assertTrue(Validator.validateEmail("abc@abc.com"));
        Assert.assertTrue(Validator.validateEmail("abc@192.33.87.56"));
        Assert.assertTrue(Validator.validateEmail("abc@abc.de"));
        Assert.assertTrue(Validator.validateEmail("abc@meetl.ink"));
        
        //invalid email addresses
        Assert.assertFalse(Validator.validateEmail("abcabc.com"));
        Assert.assertFalse(Validator.validateEmail("user@.invalid.com"));
        Assert.assertFalse(Validator.validateEmail("@abccom"));
        Assert.assertFalse(Validator.validateEmail("@invalid.com"));
    }
    
}
