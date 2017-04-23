package com.comeet.utilities;

import com.comeet.RestApiError;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class RestApiErrorMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {
        Response.StatusType statusType = getStatusType(ex); 
        
        RestApiError apiError = new RestApiError(ex.getMessage());
        
        // TODO: Does this preserve existing response headers (accept, encoding, etc?)
        return Response.status(statusType)
                .header(RestApiError.HEADER_KEY, apiError.getEncodedMessage())
                .build();
    }

    private Response.StatusType getStatusType(Throwable ex) {
        if (ex instanceof WebApplicationException) {
            return ((WebApplicationException)ex).getResponse().getStatusInfo();
        } else {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }
    
}
