package com.evenup.sample.rest.client

import groovy.json.JsonSlurper;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.evenup.sample.rest.accounts.Account;
import com.evenup.sample.rest.accounts.AccountCollection;

class RefreshAccountCollectionAction extends BaseAction {

    AccountCollection accountCollection

    def refreshAccounts(Session session) {
        sessionCheck(session)

        def path  = session.getPartnerUri() + '/accounts'
        jsonWriter.writeSent("GET", path, '')
        Response response = client.target(path)
                .request(MediaType.APPLICATION_JSON)
                .header(Session.X_EVEN_UP_TOKEN, session.sessionId)
                .get(Response.class)
                
        def json = response.readEntity(String.class)
        jsonWriter.writeReceived(response.getStatus(), json)
        if (response.getStatus() != 200) {
            // in lieu of real error handling...
            println("Unable to get ${path}: " + response.getStatus() + "," + json)
            return null;
        }
        def slurper = new JsonSlurper();
        // this response only gets us links to all our accounts (we will be adding the ability
        // to get "expand" these in the near future.  But for now, we need to go back to get
        // the actual account data.
        def obj = slurper.parseText(json)
        obj.each {
            def hrefToAccount = it.links.grep { it.rel.equals('self')}[0].href
//            jsonWriter.writeSent("GET", hrefToAccount, '')
            response = client.target(hrefToAccount)
                .request(MediaType.APPLICATION_JSON)
                .header(Session.X_EVEN_UP_TOKEN, session.sessionId)
                .get(Response.class)
            def accountJson = response.readEntity(String.class)
//            jsonWriter.writeReceived(response.getStatus(), accountJson)
            if (response.getStatus() == 200) {
                def accountFromREST = slurper.parseText(accountJson)
                def account = new Account(
                    hrefToAccount.split('/')[-1],  // get the id out of the URI 
                    accountFromREST.number, 
                    accountFromREST.referralToken, 
                    accountFromREST.accountContactNumber.number, 
                    accountFromREST.accountContactNumber.extension, 
                    accountFromREST.accountContactNumber.pin)
                accountCollection.add(account)
            }
        }

    }

}
