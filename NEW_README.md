# Qred task

### Assumption
1. I made an assumption that introducing a DB was not suggested in this task. I tried to create a model close to a DB and commented on what could transform into a table if we persist it in a DB. Right now, I'm using HashMap an in-memory DB. 
2. I used a Docker server to mock making an HTTP call to the organization info.
3. I amm using same user being an admin but I made different controller for admin. When security wil be in place properly then it will not be problem.
4. 
### Improvement
1. From the Controller to the Model, the data must pass through some mapper to abstract the outside world about our model or entity if we have one. 
2. Adding proper security (including rule-based normal user vs manager/admin) to the REST application, even authentication file-based without a database, will be sufficient for the first draft, ensuring the app is not visible to the outside world.
3. Testing (Junit and Integration testings) are very important to have it for this app. For Integration testing, we can use mock-server dependency inside during bootup. It take time to make but very interesting to test your full from request to response cycle.  
4. I used many custom exception which can potentially use to proper http response. 
3. This is an obvious choice and it is must to have a DB. Adding a database to record details, such as which customer initiated the loan application, when it was done, what the manager offered, etc. So, even if we reload the application, our app will retain previous loan application records from the DB smoothly. 
4. Including Swagger definitions for REST endpoints, even if there are few endpoints in the application. 
5. Considering metrics for the application, such as the number of requests served per minute and the time it took for processing, etc.



### How to run and test application
#### Run Program

Please run the first mock server. In the project's current directory, there is a docker-compose file that has the mock-server image, and its configuration is in the project config folder. 

Run the following instructions:
```
cd qredask
docker-compose up      // To run mockserver 

// The project will run as instructed in the old README.

Select `Use gradle wrapper task configuration`.

Add `server config/server.yaml` in the Program Arguments and you should be able to run main.   

```

#### Test application
After running the application, it will open a port on 8080 for interaction. The following curl command can be used to run a working example. 
```
1. User can put loan applciation.
 
curl --request POST \
  --url 'http://localhost:8080/api/v1/user/applications/apply?=' \
  --header 'Authorization: Basic ZmF5OjEyMzQ=' \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/8.6.0' \
  --data '{
  
  "amountApplied": {
    "currency": "SEK",
    "amount": 200000
  },
  "email": "s@gmail.com",
  "phoneNumber": "01252222",
	"organizationNumber": "559008-9800"
}'

Response will be 
{
	"id": "9c164c74-3fdb-445c-8d3e-344f72c9c0cb",
	"amountApplied": {
		"amount": 200000,
		"currency": "SEK"
	},
	"email": "s@gmail.com",
	"phoneNumber": "01252222",
	"organizationNumber": "559008-9800",
	"status": "PENDING",
	"createdAt": 1706866013.065179000
}

2. Manager can see all applciations. 

curl --request GET \
  --url http://localhost:8080/api/v1/admin/applications \
  --header 'Authorization: Basic ZmF5OjEyMzQ=' \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/8.6.0'
  
Resposne will be 
[
	{
		"id": "243f9749-249e-4a0c-ad07-58ac07d18a8f",
		"amountApplied": {
			"amount": 200000,
			"currency": "SEK"
		},
		"email": "s@gmail.com",
		"phoneNumber": "01252222",
		"organizationNumber": "559008-9800",
		"status": "PENDING",
		"createdAt": 1706866255.482743000,
		"userId": "1"
	}
]

3. Manager will  give offer

curl --request POST \
  --url http://localhost:8080/api/v1/admin/applications/applicationId/243f9749-249e-4a0c-ad07-58ac07d18a8f/offer \
  --header 'Authorization: Basic ZmF5OjEyMzQ=' \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/8.6.0' \
  --data '{
  "offerAmount": {
    "currency": "SEK",
    "amount": 2000000000
  },
  "term": "test-term-1",
  "interest": "0.1",
  "CurrencyAmount": {
    "currency": "SEK",
    "amount": 20000000
  },
  "CurrencyAmount": {
    "currency": "SEK",
    "amount": 20000000
  },
  "offerExpiredAt": "2024-02-08T12:08:08.641Z"
}'

Response will be 
{
	"id": "17b42e2d-5dca-4176-b5ae-19471679c013",
	"applicationId": "243f9749-249e-4a0c-ad07-58ac07d18a8f",
	"offerAmount": {
		"amount": 180000.00000000000444089209850062616169452667236328125000000,
		"currency": "SEK"
	},
	"term": "Dumy-term-for-now.",
	"interest": 0.12,
	"totalCommission": {
		"amount": 24000.00,
		"currency": "SEK"
	},
	"totalAmount": {
		"amount": 224000.00,
		"currency": "SEK"
	},
	"offerExpiredAt": 1707471200.720538000
}

4. User can view offer by
curl --request GET \
  --url 'http://localhost:8080/api/v1/user/applications/applicationId/243f9749-249e-4a0c-ad07-58ac07d18a8f/offer?=' \
  --header 'Authorization: Basic ZmF5OjEyMzQ=' \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/8.6.0' \
  --data '{
  
  "amountApplied": {
    "currency": "SEK",
    "amount": 200000
  },
  "email": "s@gmail.com",
  "phoneNumber": "01252222",
	"organizationNumber": "559008-9800"
}'
response:

{
	"id": "17b42e2d-5dca-4176-b5ae-19471679c013",
	"applicationId": "243f9749-249e-4a0c-ad07-58ac07d18a8f",
	"offerAmount": {
		"amount": 180000.00000000000444089209850062616169452667236328125000000,
		"currency": "SEK"
	},
	"term": "Dumy-term-for-now.",
	"interest": 0.12,
	"totalCommission": {
		"amount": 24000.00,
		"currency": "SEK"
	},
	"totalAmount": {
		"amount": 224000.00,
		"currency": "SEK"
	},
	"offerExpiredAt": 1707471200.720538000
}

5. User can sign the offer
curl --request PUT \
  --url 'http://localhost:8080/api/v1/user/applications/applicationId/74f65b23-23fb-4dfe-a6e1-3a678886480a/offerId/a5eb0f43-64e5-4cd6-bbc8-b513ff81a209/sign?=' \
  --header 'Authorization: Basic ZmF5OjEyMzQ=' \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/8.6.0' \
  --data '{
  
  "amountApplied": {
    "currency": "SEK",
    "amount": 200000
  },
  "email": "s@gmail.com",
  "phoneNumber": "01252222",
	"organizationNumber": "559008-9800"
}'

Resposne
{
	"id": null,
	"offerId": "a5eb0f43-64e5-4cd6-bbc8-b513ff81a209",
	"organizationNumber": "559008-9800",
	"name": "Ericcson AB",
	"type": "EKONOMISK",
	"offerAmount": {
		"amount": 180000.0,
		"currency": "SEK"
	},
	"term": "Dumy-term-for-now.",
	"interest": 0.12,
	"totalCommission": {
		"amount": 24000.00,
		"currency": "SEK"
	},
	"totalAmount": {
		"amount": 224000.00,
		"currency": "SEK"
	},
	"signedAt": 1706867252.116632000
}

6. User can view contract
curl --request GET \
  --url http://localhost:8080/api/v1/user/applications/applicationId/ea820eae-684d-4646-aab0-938fb6686a67/offerId/d85a6661-d6d4-4750-a09a-e9670ad3059c/contractId/038d13c2-3400-488b-8aee-e917bdff5d44 \
  --header 'Authorization: Basic ZmF5OjEyMzQ=' \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/8.6.0'
  
  Resppnse
  {
	"id": "038d13c2-3400-488b-8aee-e917bdff5d44",
	"offerId": "d85a6661-d6d4-4750-a09a-e9670ad3059c",
	"organizationNumber": "559008-9800",
	"name": "Ericcson AB",
	"type": "EKONOMISK",
	"offerAmount": {
		"amount": 180000.0,
		"currency": "SEK"
	},
	"term": "Dumy-term-for-now.",
	"interest": 0.12,
	"totalCommission": {
		"amount": 24000.00,
		"currency": "SEK"
	},
	"totalAmount": {
		"amount": 224000.00,
		"currency": "SEK"
	},
	"signedAt": 1706867546.794806000
}

```

