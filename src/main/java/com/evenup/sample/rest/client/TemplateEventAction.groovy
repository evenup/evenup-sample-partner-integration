package com.evenup.sample.rest.client

import java.nio.file.attribute.AclEntry.Builder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;

import com.evenup.sample.rest.accounts.AccountCollection;

/**
 * 
 * Copyright 2014 EvenUp, Inc.
 * 
 * THIS CODE IS INTENDED AS AN EXAMPLE ONLY.  IT HAS NOT BEEN TESTED AND 
 * SHOULD NOT BE USED IN A PRODUCTION ENVIRONMENT.
 * 
 * THE  CODE IS  PROVIDED "AS  IS",  WITHOUT WARRANTY  OF ANY  KIND, EXPRESS  
 * OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE WARRANTIES  OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *
 * @author Kevin G. McManus
 *
 */
class TemplateEventAction extends BaseAction {

    AccountCollection accountCollection
    
    
    def sendTemplateEvent(Session session, String accountGuid, String templateId, String replyTemplateId, fieldMap) {
        
        if (session.sessionId == null) {
            throw new IllegalStateException('You must login in prior to calling this.')
        }
        
        String uri = "${session.getBaseUri()}/account/${accountGuid}/events"
        
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
