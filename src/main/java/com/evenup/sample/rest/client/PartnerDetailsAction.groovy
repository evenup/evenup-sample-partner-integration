package com.evenup.sample.rest.client


import groovy.json.JsonSlurper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Gets the details for a partner object, as specified in {@link Session#getPartnerUri()}
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
class PartnerDetailsAction extends BaseAction {

    def getDetails(Session session) {

        if (session.sessionId == null) {
            throw new IllegalStateException('You must login in prior to calling this.')
        }

        jsonWriter.writeSent("GET", session.partnerUri, '')

        // We need to pass the token (sessionId) in session that we
        // received when loggin in.
        Response response = client.target(session.partnerUri)
                .request(MediaType.APPLICATION_JSON)
                .header(Session.X_EVEN_UP_TOKEN, session.sessionId)
                .get()
        
        def json = response.readEntity(String.class)
        jsonWriter.writeReceived(response.getStatus(), json)
        if (response.getStatus() != 200) {
            // in lieu of real error handling...
            println("Unable to get partner: " + response.getStatus() + "," + json)
            return null;
        }

        def slurper = new JsonSlurper()
        def result = slurper.parseText(json)

        return result
    }
}
