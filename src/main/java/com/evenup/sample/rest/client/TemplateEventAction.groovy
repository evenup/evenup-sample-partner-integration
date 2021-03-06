package com.evenup.sample.rest.client

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import com.evenup.sample.rest.accounts.AccountCollection

/**
 * Sends a template event for the given partner.  See sendTemplateEvent.
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
class TemplateEventAction extends BaseAction {

    AccountCollection accountCollection
    
    /**
     * Add a template event to the account's timeline.
     * 
     * @param session an active session
     * @param accountId EvenUp account id
     * @param templateId EvenUp template id
     * @param replyTemplateId EvenUp template id to be used for reply.  null for none.
     * @param fieldMap key are variables in the template text, with values to substitute
     * @return json object returned from EvenUp
     */
    def sendTemplateEvent(Session session, 
        String accountId, 
        String templateId, 
        String replyTemplateId, 
        Map<String, String> fieldMap) {
        
        sessionCheck(session)
        
        String uri = "${session.getBaseUri()}/account/${accountId}/events"
        
        def jsonBuilder = new JsonBuilder()
        jsonBuilder(fieldMap)
        String jsonFields = jsonBuilder.toString()
        String json = 
        """{
        "eventType":"TEMPLATE",
        "templateId":"${templateId}",
        "templateFields": ${jsonFields.toString()}
        """
        if (replyTemplateId != null) {
            json += ""","replyTemplateId": "${replyTemplateId}"}"""
        } else {
            json += '}'
        }
        
        jsonWriter.writeSent("POST", uri, json)
        Response response = client.target(uri)
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
