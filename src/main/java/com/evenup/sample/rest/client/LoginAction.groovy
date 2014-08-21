package com.evenup.sample.rest.client

import groovy.json.JsonSlurper

import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature


/**
 * Used to log in to EvenUp.  Note that {@link #login()} returns a {@link Session}
 * when it's successful and null otherwise.
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
class LoginAction extends BaseAction {
    
    def login(host, username, password) {
        return login(host, username, password, 'partnerLogin')
    }
    
    /**
     * Log in to EvenUp and start a session.
     * 
     * @param host machine and port, e.g. https://qa.letsevenup.com
     * @param username a Partner's user name
     * @param password
     * @return a new {@link Session} when successful and null otherwise.
     */
    def login(host, username, password, loginPath) {

        def baseUri = host + '/api'
        // In a production app, I would not recommend creating a Client each time.
        Client client = ClientBuilder.newClient()
        HttpAuthenticationFeature authFeature = HttpAuthenticationFeature.basic(username, password)
        client.register(authFeature)

        // This jsonWriter is used to notify others of the JSON going to/from
        // the server.
        jsonWriter.writeSent('POST', baseUri + "/${loginPath}", '')

        // To log in to EvenUp as a partner, we POST to <host>/api/partnerLogin, using
        // HTTP Basic Auth (see above).
        Response response = client.target(baseUri).path(loginPath)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity("", MediaType.APPLICATION_JSON))

        def json = response.readEntity(String.class)
        jsonWriter.writeReceived(response.getStatus(), json)
        if (response.getStatus() != 200) {
            println "Unable to login ${response.getStatus()}"
            return null;
        }

        // This is our session token, which needs to be pass to 
        // subsequent requests.  Every HTTP framework should have 
        // a mechanism to do this - get an http header value.
        String sessionId = response.getHeaderString(Session.X_EVEN_UP_TOKEN)

        // JsonSlurper is a nifty Groovy utility that puts JSON into an object structure
        // In this case, we get back an array of links, the first (and only)
        // of which points to the Partner that just logged in.
        // I am packing all this into a Session object that I can reference
        // for my other requests.
        def slurper = new JsonSlurper()
        def result = slurper.parseText(json)
        String partnerUri = result.links[0].href
        return new Session(baseUri: baseUri, partnerUri: partnerUri, sessionId: sessionId)
    }

    // used only for testing.
    static main(args) {
        Session session = new LoginAction().login("http://127.0.0.1:9000", "partner@letsevenup.com", "p")
        println session.sessionId
        println session.baseUri
        println session.partnerUri
    }

}
