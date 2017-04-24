package com.comeet.utilities;

import com.comeet.RestApiError;
import com.comeet.RestApiErrorDetail;
import com.comeet.RestApiErrorResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class RestApiErrorMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {
        Response.StatusType statusType = getStatusType(ex); 
        
        int code = statusType.getStatusCode();
        
        RestApiError apiError = new RestApiError(ex.getMessage(), code);
        RestApiErrorDetail apiErrorDetail = new RestApiErrorDetail(ex);
        apiError.getErrors().add(apiErrorDetail);
        
        String encodedMessage = urlEncode(apiError.getMessage());
        
        return Response.status(statusType)
                .header(RestApiError.HEADER_KEY, encodedMessage)
                .type(MediaType.APPLICATION_JSON)
                .entity(new RestApiErrorResponse(apiError))
                .build();
    }

    private Response.StatusType getStatusType(Throwable ex) {
        if (ex instanceof WebApplicationException) {
            return ((WebApplicationException)ex).getResponse().getStatusInfo();
        } else {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * Gets the message in a format safe for transmission in an HTTP header.
     * @param Input String to encode.
     * @return Encoded string.
     */    
    private String urlEncode(String input) {
        try {
            return java.net.URLEncoder.encode(input, java.nio.charset.StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            assert e == null;
            return "";
        }
    }
    
}
