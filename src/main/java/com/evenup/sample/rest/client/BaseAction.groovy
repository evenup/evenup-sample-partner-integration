package com.evenup.sample.rest.client

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

/**
 * The base of all "actions".  Each action makes a REST call to EvenUp.
 * The jsonWriter should be set when instantiating a child class.  For example:
 * 
 * <pre>{@code
 * def action = new ChildAction(jsonWriter: someWriter)
 * }</pre>
 * 
 * <br>
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
abstract class BaseAction {
    
    // TODO figure out the scope for this...
    // I don't think we should be creating one
    // per request.
    Client client = ClientBuilder.newClient()
    
    JsonWriter jsonWriter = new JsonWriter() {

        void writeReceived(int status, String json) {
            println "RESPONSE STATUS: ${status}\nJSON RETURNED: ${json}"
        }

        void writeSent(String verb, String uri, String json) {
            println "${verb}'d to ${uri}: \n ${json}"
        }
    }
}
