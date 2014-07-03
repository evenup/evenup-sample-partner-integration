package com.evenup.sample.rest.client

import groovy.json.JsonSlurper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Can be used to perform a RESTful GET on a URI that the partner is 
 * permitted to.
 * <br><br>
 * Copyright 2014 EvenUp, Inc.
 * <br><br>
 * THIS CODE IS INTENDED AS AN EXAMPLE ONLY.  IT HAS NOT BEEN TESTED AND 
 * SHOULD NOT BE USED IN A PRODUCTION ENVIRONMENT.
 * <br><br>
 * THE  CODE IS  PROVIDED "AS  IS",  WITHOUT WARRANTY  OF ANY  KIND, EXPRESS  
 * OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE WARRANTIES  OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *
 * @author Kevin G. McManus
 *
 */
class GenericGetAction extends BaseAction {

    def get(Session session, String uri) {
        sessionCheck(session)
        
        jsonWriter.writeSent("GET", uri, '')

        Response response = client.target(uri)
                .request(MediaType.APPLICATION_JSON)
                .header(Session.X_EVEN_UP_TOKEN, session.sessionId)
                .get()

        def json = response.readEntity(String.class)
        jsonWriter.writeReceived(response.getStatus(), json)

        if (response.getStatus() != 200) {
            // in lieu of real error handling...
            println("Unable to get ${uri}: " + response.getStatus() + "," + json)
            return null;
        }

        def slurper = new JsonSlurper()
        def result = slurper.parseText(json)

        return result
    }
}
