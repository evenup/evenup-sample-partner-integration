package com.evenup.sample.rest.client.swing

import groovy.json.JsonException;
import groovy.json.JsonOutput;

import javax.swing.JTextArea;
import javax.ws.rs.core.Response;

import com.evenup.sample.rest.client.JsonWriter


/**
 * Writes JSON/REST response data to the given {@link JTextArea}s.
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
class SwingJsonWriter implements JsonWriter {

    JTextArea from
    JTextArea to

    public void writeReceived(int status, String json) {
        try {
            from.text = "RESPONSE STATUS: ${status}\n" + JsonOutput.prettyPrint(json)
        } catch (JsonException e) {
            println "Unable to parse: ${json}"
            println e
        }
    }

    public void writeSent(String verb, String uri, String json) {
        json = JsonOutput.prettyPrint(json)
        to.text = "${verb} to ${uri}: \n ${json}"
    }
}
