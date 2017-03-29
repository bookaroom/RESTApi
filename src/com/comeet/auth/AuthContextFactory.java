package com.comeet.auth;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;

public class AuthContextFactory {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    // Bearer token format defined in https://tools.ietf.org/html/rfc6750#section-2.1
    private static final String BEARER_TOKEN_PREAMBLE = "Bearer ";
    private static final String BEARER_VALUE_REGEX = "[-._~+/A-Za-z0-9]+=*";

    /**
     * Builds an authentication context from HTTP headers.
     * 
     * @param headers The headers object.
     * @return An AuthContext.
     * @throws AuthContextException When no supported authorization mechanism is found in the
     *         provided request.
     */
    public AuthContext buildContext(HttpHeaders headers) throws AuthContextException {
        List<String> authValues = headers.getRequestHeader(AUTHORIZATION_HEADER_NAME);

        if (authValues != null) {
            for (String authValue : authValues) {
                // Bearer token format defined in https://tools.ietf.org/html/rfc6750#section-2.1
                if (authValue != null && authValue.length() > BEARER_TOKEN_PREAMBLE.length()
                                && authValue.startsWith(BEARER_TOKEN_PREAMBLE)) {

                    String bearerValue = authValue.substring(BEARER_TOKEN_PREAMBLE.length()).trim();

                    if (bearerValue.matches(BEARER_VALUE_REGEX)) {
                        AuthContext context = new AuthContext();
                        context.setBearerToken(authValue);
                        return context;
                    }
                }
            }

        }
        throw new AuthContextException(
                        "No valid (or supported) authentication information found in HTTP headers.");
    }

}
