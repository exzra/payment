# Payment center

___________

# Task

Design and implement a RESTful API (including data model and the backing implementation) for
money transfers between accounts.

___________

# API description

## get account
    curl --request GET \
      --url http://localhost:8080/payment/{UUID}
  
## create account
    curl --request POST \
      --url http://localhost:8080/payment \
      --header 'content-type: application/json' \
      --data '{
        "value": {initial long value}
    }'

## update account ()
    curl --request PUT \
      --url http://localhost:8080/payment \
      --header 'content-type: application/json' \
      --data '{
        "id": {UUID},
        "diff": {exchange value (negative or positive)}
    }'

## transfer money
    curl --request POST \
      --url http://localhost:8080/transfer \
      --header 'content-type: application/json' \
      --data '{
        "senderID": {UUID},
        "receiverID": {UUID},
        "value": {long value}
    }'
__________________

# Implementation details

* Spark Java for REST 
* No DI frameworks to do more thin JAR
* Storage implementation: HashMap with synchronized blocks
    * final Service class
    and private final field storage to prevent access to storage and changes from other places except this class
    * according to the fact that jetty does not share threads ( what exactly do some reactive frameworks )
     between processes we can use just synchronized blocks - for simplicity
* Notice that get account can return incorrect value ( as most of the banking systems do )
     
_________________

# Tests

* PaymentControllerTest contains something like integration tests
    * Tests create/read/update operations
    * Tests single transfer request
    * Tests multiple threads requests for transfer
* PaymentServiceTest contains failure scenarios because happy paths were tested in controller
