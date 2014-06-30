package com.evenup.sample.rest.client

import groovy.json.JsonSlurper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Adds one invitation code to the partner's collection.  Always adds to "BATCH1".
 * The generated invitation code is returned as part of the JSON object that 
 * is returned.
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
class AddInvitationAction extends BaseAction {

    def addInvite(Session session, String accountNumber) {

        if (session.sessionId == null) {
            throw new IllegalStateException('You must login in prior to calling this.')
        }

        // Invitations are are posted as an array of JSON objects.
        // This is an array of one.  Note, that this is as simple
        // as it gets, with not email or welcome events.
        String json = '''[ 
      {
        "firstName":"Test",
        "lastName":"User",
        "zip":"12345",
        "accountNumber":"replaceMe"
      }
    ]'''.replace('replaceMe', accountNumber)


        // "BATCH1" is just a mechanism for scoping invitations,
        // i.e. adding them in batches.  You can use this id again.
        // It's simply a way to scope invitations.  Name it what you
        // like, but remember it.
        String path = session.partnerUri + '/invitations/BATCH1'
        jsonWriter.writeSent("POST", path, json)
        Response response = client.target(path)
                .request(MediaType.APPLICATION_JSON)
                .header(Session.X_EVEN_UP_TOKEN, session.sessionId)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON))

        // The server returns what was set.
        json = response.readEntity(String.class)
        jsonWriter.writeReceived(response.getStatus(), json)

        if (response.getStatus() != 201) {
            // in lieu of real error handling...
            return null;
        }


        def slurper = new JsonSlurper()
        def result = slurper.parseText(json)
        return result
    }
}
