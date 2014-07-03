package com.evenup.sample.rest.client

import groovy.json.JsonSlurper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Sets the REST Callback end point for the partner in session.
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
class SetRESTCallbackAction extends BaseAction {

    /**
     * Sets the REST Callback end point for the partner in session.
     * 
     * @param session a valid {@link Session}
     * @param uri the uri that EvenUp will call with event notifications.
     * @return an object representation of the return json.
     */
    def setRESTCallback(Session session, String uri) {

        sessionCheck(session)
        
        // When we POST this, it will add this to "notification methods".
        // N.B. We are using the sessionId from logging in.
        String json
        if (uri == null || uri.length() < 1) {
            json = '{"restCallback": null}'
        } else {
            json = "{\"restCallback\": \"${uri}\"}"
        }

        String path = session.partnerUri + '/notification'
        jsonWriter.writeSent("POST", path, json)
        Response response = client.target(path)
                .request(MediaType.APPLICATION_JSON)
                .header(Session.X_EVEN_UP_TOKEN, session.sessionId)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON))

        json = response.readEntity(String.class)
        jsonWriter.writeReceived(response.getStatus(), json)
        // 201 means something was "created".  Checking 200 too,
        // in case we decide that "OK" (200) is a better way to
        // respond.
        if (!response.getStatus() in [200, 201]) {
            // in lieu of real error handling...
            println ("Unable to set REST Callback: " + response.getStatus() + "," + json)
            return null;
        }

        def slurper = new JsonSlurper()
        def result = slurper.parseText(json)

        return result

    }
}
