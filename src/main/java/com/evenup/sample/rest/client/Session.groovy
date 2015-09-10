package com.evenup.sample.rest.client

/**
 * Holds what is needed for a Session.  {@link LoginAction#login}
 * returns a new Session, which is then passed to 
 * every other action.
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
class Session {
    static final String X_EVEN_UP_TOKEN = "X-EvenUp-Token"
    
    String sessionId
    String partnerUri
    String baseUri
    String memberUri
    
}
