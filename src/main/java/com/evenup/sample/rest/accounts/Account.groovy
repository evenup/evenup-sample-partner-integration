package com.evenup.sample.rest.accounts

import groovy.transform.Immutable


/**
 * A little immutable class to ferry Account data from the REST Server 
 * to the app, as well as store it for later use.
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
@Immutable class Account implements Serializable {

    String accountResourceURI;
    String accountNumber;
    String token;
    String acn;
    String acnExtension;
    String acnPass;
    
}
