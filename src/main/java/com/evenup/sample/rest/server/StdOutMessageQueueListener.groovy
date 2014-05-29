package com.evenup.sample.rest.server

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.JTextArea;

/**
 * Writes the strings from the Q to std out.
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
class StdOutMessageQueueListener implements Runnable {

    private BlockingQueue messageQ

    boolean stop = false

    StdOutMessageQueueListener(Queue messageQ) {
        this.messageQ = messageQ
    }

    public void run() {
        while(!stop) {
            def message = this.messageQ.poll(300, TimeUnit.MILLISECONDS)
            if (message != null) {
                println('\n--------------------\n')
                println(message)
            }
        }
    }
}
