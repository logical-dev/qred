# Qred Flexible Programming Task

The sample code is using dropwizard to expose a rough implementation of a few endpoints.
The functionality exposed is:
* Apply for loan.
* Get the latest application, including the offer details.
* Sign the application
* Get all signed contracts

While the Application model and handling was good enough until recently, we realized that it grows too big and it's getting difficult to maintain.

We would like to be able to trust our models and we decided to separate the domain/business logic from the communication layer, introduce proper variable types and validation.

Our domain contains the following:
* A user can apply for a loan.
* A Qred Account Manager reviews the application and makes an offer.
* A user can review and negotiate the offer. The user can either negotiate the amount or the term.
* A user can accept & sign the offer. 
* A user can see his/her Contracts.


In order for a user to apply for a loan they need to provide:
* amount, minimum: 10000, maximum:250000
* email
* phone number
* valid organization number

A valid organization number looks like this: 559008-9800 or 5590089800

An application should be accepted only if the organization number belongs to an existing company.
Today we use the allabolag api to validate the organization number and collect the organization name and type. 

You can create a dummy service instead of connecting to the allabolag api.

An Offer from an Account Manager includes:
* amount (with no commission)
* term
* interest
* total commission = amount + interest
* total amount = amount + total commission
* day of expiration (7 days after it was created)

A Contract includes:
* organization name
* organization number
* organization type
* amount (with no commission)
* term
* interest 
* total commission = amount*interest
* total amount = amount + total commission
* date of signature


You are encouraged to restructure and refactor the application code, introduce your own models, repositories, resources and/or delete any part of the sample code.
The application sample is missing logs, metrics and tests. Feel free to cover any or none of these.

##### Technologies Used:
* Dropwizard 1.2.3 including the full dependency tree (jetty, jersey, logback, jackson, metrics, etc)
* Java 1.8
* Gradle


Create a new  project from existing sources to IntelliJ.

Select `Use gradle wrapper task configuration`.

Add `server config/server.yaml` in the Program Arguments and you should be able to run main.

You can use Postman or any other HTTP client to do HTTP requests 


##### The project also includes:
* A dummy User.
* A working basic authenticator.
* An unfinished custom validator.

