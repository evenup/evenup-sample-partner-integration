package com.evenup.sample.rest.accounts

import groovy.transform.Immutable


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
@Immutable class Account implements Serializable {

    String accountGuid;
    String accountNumber;
    String token;
    String firstName;
    String lastName;
    String acn;
    String acnExtension;
    String acnPass;
    
}
