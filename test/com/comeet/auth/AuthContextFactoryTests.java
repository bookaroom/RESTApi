package com.comeet.auth;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.ws.rs.core.HttpHeaders;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;



@RunWith(MockitoJUnitRunner.class)
public class AuthContextFactoryTests extends AuthContextFactory {

    // Maximum practical HTTP header length is 16kb
    // http://stackoverflow.com/questions/686217/maximum-on-http-header-values
    private static final int MAX_HEADER_LENGTH = 1024 * 16;

    @Mock
    HttpHeaders mockHeaders;

    @Test
    public void testBuildBasicAuthContext() throws Exception {
        final String anyDecodedPair = "foo:bar";
        final String anyEncodedPair = new String(Base64.getMimeEncoder().encode(anyDecodedPair.getBytes()));
        final String anyBasicAuthHeader = "Basic " + anyEncodedPair;

        // Setup

        when(mockHeaders.getRequestHeader(AUTHORIZATION_HEADER_NAME))
                        .thenReturn(new ArrayList<String>(Arrays.asList(anyBasicAuthHeader)));

        // Act
        AuthContextFactory factory = new AuthContextFactory();
        AuthContext resultContext = factory.buildContext(mockHeaders);

        // Verify
        Assert.assertNotNull(resultContext);

        Assert.assertEquals(anyEncodedPair, resultContext.getBasicEncoded());
        Assert.assertEquals(anyDecodedPair, resultContext.getBasicDecoded());
        Assert.assertEquals(anyDecodedPair.split(":")[0], resultContext.getBasicUsername());
        Assert.assertEquals(anyDecodedPair.split(":")[1], resultContext.getBasicPassword());

        Assert.assertNull(resultContext.getBearerToken());
    }

    @Test
    public void testBuildBearerAuthContext() throws Exception {
        // Use a deterministic random seed.
        Random random = new Random(AUTHORIZATION_HEADER_NAME.hashCode());

        // Repeat this test to exercise the parsing code.
        for (int i = 0; i < 100; i++) {
            final String anyBearerToken = generateBearerToken(random);
            final String anyBearerAuthHeader = "Bearer " + anyBearerToken;
    
            // Setup
            when(mockHeaders.getRequestHeader(AUTHORIZATION_HEADER_NAME))
                            .thenReturn(new ArrayList<String>(Arrays.asList(anyBearerAuthHeader)));
    
            // Act
            AuthContextFactory factory = new AuthContextFactory();
            AuthContext resultContext = factory.buildContext(mockHeaders);
    
            // Verify
            Assert.assertNotNull(resultContext);
            Assert.assertEquals(anyBearerToken, resultContext.getBearerToken());
        }
    }
    
    /**
     * Generates a valid, but nonsense, bearer token.
     * @return A string of random length > 1 whose format matches https://tools.ietf.org/html/rfc6750#section-2.1 
     */
    private String generateBearerToken(Random random) {
        final String allowedUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~+/";
        final String allowedAll = allowedUpper.toUpperCase() + allowedUpper.toLowerCase();
        final String allowedSuffix = "=";
        
        StringBuilder sb = new StringBuilder();
        int length = random.nextInt(MAX_HEADER_LENGTH) + 1;
        int suffixLength = random.nextInt(length - 1);
        for (int i = 0; i < length - suffixLength; i++) {
            sb.append(allowedAll.charAt(random.nextInt(allowedAll.length() - 1)));
        }
        
        for (int i = 0; i < suffixLength; i++) {
            sb.append(allowedSuffix);
        }
        
        return sb.toString();
    }

    @Test
    public void testBuildAuthContext_NoHeaders() {

        // Setup
        when(mockHeaders.getRequestHeader(AUTHORIZATION_HEADER_NAME)).thenReturn(new ArrayList<String>());

        // Act
        AuthContextFactory factory = new AuthContextFactory();
        try {
            AuthContext resultContext = factory.buildContext(mockHeaders);

            // Verify
            Assert.fail("AuthContext should throw exception when encountering no auth headers.");
        } catch (AuthContextException e) {
            Assert.assertNotNull(e);
        }
    }
}
