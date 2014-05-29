package com.evenup.sample.rest.client.swing

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.JTextArea;

/**
 * Listens to the message Q, pops items from it and writes them
 * to the {@link JTextArea}.
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
class SwingMessageQueueListener implements Runnable {
    
    private BlockingQueue messageQ
    
    boolean stop = false

    private JTextArea sink;
        
    SwingMessageQueueListener(Queue messageQ, JTextArea sink) {
        this.sink = sink;
        this.messageQ = messageQ
    }

    public void run() {
        while(!stop) {
            def message = this.messageQ.poll(300, TimeUnit.MILLISECONDS)
            if (message != null) {
                sink.append('\n--------------------\n')
                sink.append(message)
            }
        }
    }
}
