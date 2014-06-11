package com.evenup.sample.rest.server;

import java.util.concurrent.BlockingQueue;

import groovy.json.JsonException;
import groovy.json.JsonOutput;
import groovy.json.JsonSlurper;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * A simple REST resource that listens for EvenUp events on the 
 * path - "/events".  For now, it just puts the JSON for these
 * events on a Q to be displayed in either the client GUI
 * or the console.
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
@Path("/events")
public class EventCallbackResource {

    JsonSlurper slurper = new JsonSlurper()
    
    // Should use Jersey's built-in DI framework.
    // However, this is a sample...
    static BlockingQueue<String> messageQ
    
    /**
     * This resource just takes those callback events from 
     * EvenUp and puts them as a String on a {@link BlockingQueue}
     * @param jsonText
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    def Response addEventCallback(String jsonText) {
        
        messageQ.add(JsonOutput.prettyPrint(jsonText))
        
        // we should just swallow errors here:
        def jsonObj
        try {
            jsonObj = slurper.parseText(jsonText)
        } catch (JsonException e) {
            messageQ.add("Unable to parse JSON in event: ${jsonText}")
            return Response.ok().build();
        }
        
        // In a real environment, I would set up DTO objects to hold this data.
        // Groovy allows me to deal with it quickly, just creating objects on 
        // the fly. 
        if (jsonObj.type.equals("ACCOUNT_CREATED")) {
            println "Someone has activated their invitation (${jsonObj.token}) in EvenUp!  Their account guid is ${jsonObj.accountGuid}"
            // TODO store the link b/twn account number and EvenUp's guid.  This
            // will allow me to query and send events to that account later. 
        }
        
        return Response.ok().build();
    }
}
