package com.evenup.sample.rest.client

import groovy.json.JsonSlurper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

class GenericGetAction extends BaseAction {

    def get(Session session, String uri) {
        if (session.sessionId == null) {
            throw new IllegalStateException('You must login in prior to calling this.')
        }

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
