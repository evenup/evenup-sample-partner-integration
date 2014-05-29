package com.evenup.sample.rest.client.swing

import groovy.swing.SwingBuilder

import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

import javax.swing.*

import com.evenup.sample.rest.client.AddInvitationAction;
import com.evenup.sample.rest.client.JsonWriter
import com.evenup.sample.rest.client.LoginAction
import com.evenup.sample.rest.client.PartnerDetailsAction
import com.evenup.sample.rest.client.Session;
import com.evenup.sample.rest.client.SetRESTCallbackAction;
import com.evenup.sample.rest.server.JettyRESTServer

/**
 * This is the main entry point into the client GUI.  The
 * GUI components are all defined in {@link #run()}.
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
class MainClientView {

    Executor executor = Executors.newFixedThreadPool(4);

    JettyRESTServer restServer
    BlockingQueue<String> messageQ = new LinkedBlockingQueue<String>()
    SwingMessageQueueListener mqListener

    LoginAction loginAction
    SetRESTCallbackAction setRESTCallbackAction
    AddInvitationAction addInvitationAction
    PartnerDetailsAction partnerDetailsAction

    // used to build all Swing objects.
    def swing = new SwingBuilder()

    def frame
    // The session hold a few bits of data and is a result of logging in
    // see LoginAction.
    Session session

    // holds a copy of the data sent to EvenUp on the last request
    JTextArea toREST

    // holds a copy of the data received from EvenUp on the last request
    JTextArea fromREST

    // holds all callbacks received from EvenUp.  This appends...
    JTextArea callbackREST

    // a goofy interface that is used to write to the toRest and
    // fromRest textAreas
    JsonWriter jWriter

    def login() {
        def loginDialog = swing.dialog(title: 'Login', modal:true, alwaysOnTop: true, locationRelativeTo: null, resizable: false)
        def panel = swing.panel{
            vbox {
                hbox{
                    label(text: 'Host ')
                    textField(columns: 20, id: 'host')
                }
                hbox{
                    label(text: 'Email ')
                    textField(columns: 20, id: 'name')
                }
                hbox{
                    label(text: 'Password ')
                    passwordField(columns: 20, id: 'password')
                }
                hbox{
                    button('OK', actionPerformed: {session = loginAction.login(host.text, name.text, password.text); loginDialog.dispose()})
                    button('Cancel', actionPerformed: {loginDialog.dispose()})
                }
            }
        }
        loginDialog.getContentPane().add(panel)
        loginDialog.pack()
        loginDialog.show()
    }

    def showPartnerDetails() {
        if (session == null) {
            def pane = swing.optionPane(message:'You must log in first.')
            def dialog = pane.createDialog(frame, 'Error')
            dialog.show()
        } else {
            partnerDetailsAction.getDetails(session)
        }
    }

    def setRESTCallback() {
        if (session == null) {
            def pane = swing.optionPane(message:'You must log in first.')
            def dialog = pane.createDialog(frame, 'Error')
            dialog.show()
        } else {
            def serverDialog = swing.dialog(title: 'Set REST Callback Endpoint', modal:true, alwaysOnTop: true, locationRelativeTo: null, resizable: false)
            def panel = swing.panel{
                vbox {
                    hbox{
                        label(text: 'URI ')
                        textField(columns: 20, id: 'uri')
                    }
                    hbox{
                        button('OK', actionPerformed: {setRESTCallbackAction.setRESTCallback(session, uri.text); serverDialog.dispose()})
                        button('Cancel', actionPerformed: {serverDialog.dispose()})
                    }
                }
            }
            serverDialog.getContentPane().add(panel)
            serverDialog.pack()
            serverDialog.show()
        }
    }

    def addInvitation() {
        if (session == null) {
            def pane = swing.optionPane(message:'You must log in first.')
            def dialog = pane.createDialog(frame, 'Error')
            dialog.show()
        } else {
            def serverDialog = swing.dialog(title: 'Add Invitation', modal:true, alwaysOnTop: true, locationRelativeTo: null, resizable: false)
            def panel = swing.panel{
                vbox {
                    hbox{
                        label(text: 'Account Number ')
                        textField(columns: 20, id: 'accountNumber')
                    }
                    hbox{
                        button('OK', actionPerformed: {addInvitationAction.addInvite(session, accountNumber.text); serverDialog.dispose()})
                        button('Cancel', actionPerformed: {serverDialog.dispose()})
                    }
                }
            }
            serverDialog.getContentPane().add(panel)
            serverDialog.pack()
            serverDialog.show()
        }
    }

    def startServer() {
        if (restServer.isRunning()) {
            def pane = swing.optionPane(message:'REST server is already running.')
            def dialog = pane.createDialog(frame, 'Error')
            dialog.show()
            return
        }

        def serverDialog = swing.dialog(title: 'Start REST Server', modal:true, alwaysOnTop: true, locationRelativeTo: null, resizable: true)
        def panel = swing.panel (preferredSize: [350, 150]){
            vbox {
                hbox{
                    label(text: 'Port ')
                    textField(columns: 20, id: 'port')
                }
                hbox{
                    textArea(text: 'This will start an HTTP server to handle REST calls from EvenUp.  Note that your firewall will need to allow traffic to this port to be successful.',
                    lineWrap: true, wrapStyleWord: true)
                }
                hbox{
                    button('OK', actionPerformed: {restServer.setPort(Integer.valueOf(port.text));executor.execute(restServer); serverDialog.dispose()})
                    button('Cancel', actionPerformed: {serverDialog.dispose()})
                }
            }
        }
        serverDialog.getContentPane().add(panel)
        serverDialog.pack()
        serverDialog.show()
    }

    def stopServer() {
        restServer.stop();
    }

    def close() {
        stopServer();
        mqListener.stop = true
    }

    def createActions(jWriter) {
        loginAction = new LoginAction(jsonWriter: jWriter)
        setRESTCallbackAction = new SetRESTCallbackAction(jsonWriter: jWriter)
        addInvitationAction = new AddInvitationAction(jsonWriter: jWriter)
        partnerDetailsAction = new PartnerDetailsAction(jsonWriter: jWriter)
    }

    /**
     * This my first attempt at using SwingBuilder.  It is meant to 
     * set up a simple frame that has a bunch of menu items to perform 
     * REST calls to EvenUp's Servers.
     * 
     */
    void run() {
        swing.edt {
            frame = frame(title: 'Partner REST Test', defaultCloseOperation: JFrame.EXIT_ON_CLOSE,
            size: [800, 600], show: true, locationRelativeTo: null, windowClosed: {close()}) {
                lookAndFeel("system")
                menuBar() {
                    menu(text: "File", mnemonic: 'F') {
                        menuItem(text: "Exit", mnemonic: 'X', actionPerformed: {dispose() })
                    }
                    menu(text: "Partner", mnemonic: 'P') {
                        menuItem(text: 'Login', mnemonic: 'L', actionPerformed: {login() })
                        menuItem(text: 'View Details', mnemonic: 'D', actionPerformed: {showPartnerDetails()})
                        menuItem(text: 'Set REST Callback', mnemonic: 'N', actionPerformed: {setRESTCallback()})
                        menuItem(text: 'Add Invitation', mnemonic: 'I', actionPerformed: {addInvitation()})
                    }
                    menu(text: "Callback Server") {
                        menuItem(text: 'Start', mnemonic: 'S', actionPerformed: {startServer()})
                        menuItem(text: 'Stop', mnemonic: 'T', actionPerformed: {stopServer()})
                    }
                }

                splitPane (orientation:JSplitPane.VERTICAL_SPLIT, dividerLocation:400){

                    splitPane (dividerLocation:400) {
                        scrollPane(constraints: "left", border: titledBorder(title:  'TO SERVER')) {
                            toREST = textArea(editable: false, lineWrap: true)
                        }
                        scrollPane(constraints: "right", border: titledBorder(title: 'FROM SERVER')) {
                            fromREST = textArea(editable: false, lineWrap: true)
                        }
                    }
                    scrollPane(constraints: "bottom", border: titledBorder(title:  'REST CALLBACKS')) {
                        callbackREST = textArea(editable: false, lineWrap: true)
                    }
                }
            }
        }

        jWriter = new SwingJsonWriter(from: fromREST, to: toREST)
        mqListener = new SwingMessageQueueListener(messageQ, callbackREST)
        executor.execute(mqListener)
        createActions(jWriter)
        // i made 9000 the default, but it should get set by the gui
        restServer = new JettyRESTServer(9000, messageQ)
    }

    static void main(args) {
        def mv = new MainClientView()
        mv.run()
    }
}