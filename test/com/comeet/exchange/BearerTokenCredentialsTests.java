package com.comeet.exchange;

import static org.mockito.Mockito.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.ws.rs.core.HttpHeaders;

import microsoft.exchange.webservices.data.core.request.HttpWebRequest;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BearerTokenCredentialsTests {

    private static final String SIMPLE_TOKEN = "Zm9vLmJhcg==";

    @Test
    public void testConstruct_ValidToken() {

        ExchangeCredentials creds = new BearerTokenCredentials(SIMPLE_TOKEN);

        Assert.assertNotNull(creds);
    }

    @Test
    public void testConstruct_NullToken() {
        try {
            ExchangeCredentials creds = new BearerTokenCredentials(null);
            Assert.fail("Expected exception constructing BearerTokenCredentials from null.");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("null"));
        }
    }

    @Test
    public void testPrepareWebRequest_Headers() throws URISyntaxException {
        final String authorizationHeaderKey = "Authorization";
        final String bearerTokenPrefix = "Bearer";
        final String anyValidToken = SIMPLE_TOKEN;
        HttpWebRequest mockRequest = mock(HttpWebRequest.class);

        final ArrayList<HashMap<String, String>> headersContainer = new ArrayList<>();
        headersContainer.add(new HashMap<>());
        when(mockRequest.getHeaders()).thenReturn(headersContainer.get(0));
        doAnswer(invocation -> {
            headersContainer.set(0, (HashMap<String, String>) invocation.getArguments()[0]);
            return null;
        }).when(mockRequest).setHeaders(any());

        ExchangeCredentials creds = new BearerTokenCredentials(anyValidToken);

        creds.prepareWebRequest(mockRequest);

        Map<String, String> actualHeaders = mockRequest.getHeaders();
        Assert.assertTrue("Headers must contain authenticate line.",
                        actualHeaders.containsKey(authorizationHeaderKey));
        String actualAuthorizationHeader = actualHeaders.get(authorizationHeaderKey);
        Assert.assertTrue("Header value must start with " + bearerTokenPrefix, 
                        actualAuthorizationHeader.startsWith(bearerTokenPrefix));
        Assert.assertTrue("Header value must contain token string.",
                        actualAuthorizationHeader.contains(anyValidToken));
    }
}
