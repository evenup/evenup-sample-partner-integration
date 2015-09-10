package com.evenup.sample.rest.client

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import com.evenup.sample.rest.accounts.Account;
import com.evenup.sample.rest.accounts.AccountCollection;

import spock.lang.Specification


class RefreshAccountCollectionActionTest extends RESTClientSpecification {
    
    private RefreshAccountCollectionAction rac

    AccountCollection accountCollection = Mock()
    Session session = new Session(sessionId: 'SESSION_ID')
    
    def setup() {
        rac = new RefreshAccountCollectionAction(
            accountCollection: accountCollection, 
            client: client)
    }
    
    def "test no accounts"() {
        setup:
        Response resp = Mock()
        builder.get(Response.class) >> resp
        resp.readEntity(String.class) >> '[]'
        resp.getStatus() >> 200 
        
        when:
        rac.refreshAccounts(session)
        
        then:
        0 * accountCollection.add(_)
    }
    
    def "test successful add"() {
        setup:
        Account expectedAccount = new Account(
                '5a1d910c-2faf-4fd8-a581-69961336c4d7', 
                '10007',
                'U5ER3',
                "13037474276",
                "6959",
                "0007")
        
        Response resp1 = Mock()
        resp1.getStatus() >> 200
        resp1.readEntity(String.class) >> '''
[
  {
    "links":[
      {
        "rel":"self",
        "href":"http://192.168.30.10/api/account/5a1d910c-2faf-4fd8-a581-69961336c4d7"
      }
    ],
    "name":"My Car Loan"
  }
]        
'''
        Response resp2 = Mock()
        resp2.getStatus() >> 200
        resp2.readEntity(String.class) >> '''
{
  "links":[
    {
      "rel":"self",
      "href":"http://192.168.30.10/api/account/5a1d910c-2faf-4fd8-a581-69961336c4d7"
    },
    {
      "rel":"partner",
      "href":"http://192.168.30.10/api/partner/0001d468-b083-4151-a684-04a1d4d88fd7"
    },
    {
      "rel":"account-events",
      "href":"http://192.168.30.10/api/account/5a1d910c-2faf-4fd8-a581-69961336c4d7/events"
    },
    {
      "rel":"member",
      "href":"http://192.168.30.10/api/member/0afcb537-7677-4b34-82e0-72833bd6f107"
    },
    {
      "rel":"add-account-event",
      "href":"http://192.168.30.10/api/account/5a1d910c-2faf-4fd8-a581-69961336c4d7/events"
    }
  ],
  "creationTime":1400716659646,
  "name":"My Car Loan",
  "number":"10007",
  "state":"ACTIVE",
  "referralToken":"U5ER3",
  "accountContactNumber":{
    "number":"13037474276",
    "extension":"6959",
    "pin":"0007"
  }
}
'''
        builder.get(Response.class) >>> [resp1, resp2]
        
        when:
        rac.refreshAccounts(session)
        
        then:
        1 * accountCollection.add(expectedAccount)
    }
}
