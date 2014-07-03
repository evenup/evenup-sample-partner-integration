package com.evenup.sample.rest.client

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;

import spock.lang.Specification

class RESTClientSpecification extends Specification {
    
    Client client = Mock()
    WebTarget target = Mock()
    Builder request = Mock()
    Builder builder = Mock()
    
    
    void setup() {
        client.target(_) >> target
        target.request(MediaType.APPLICATION_JSON) >> request
        request.header(Session.X_EVEN_UP_TOKEN, _) >> builder
    }
        
}
