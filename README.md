# BankTransactions

## Overview
REST API of Bank Transactions project


### Server settings
*Host* : localhost  
*Port* : 8080 


## Paths


### Register
```
POST /api/login
```


#### Description
Registers new user with provided credentials


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**user**  <br>*required*|user credentials|[User](#user)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|User was successfully created|none|
|**400**|Invalid username and/or password|string|


### Login
```
POST /api/login
```


#### Description
Authorizes user by username and password and generates access token


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**username**  <br>*required*|user name|string|
|**Path**|**password**  <br>*required*|password|string|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Login was successful|uuid|
|**401**|Incorrect username and/or password|string|


### Get balance
```
GET /account/balance
```


#### Description
Returns current balance for account


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**accountId**  <br>*required*|Bank account id|uuid|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Account was found in database|BigDecimal|
|**404**|Account is not present in database|string|


### Get statement
```
GET /account/statement
```


#### Description
Returns list of transactions for account


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**accountId**  <br>*required*|Bank account id|uuid|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Account was found|List<[Transaction](#transaction)>|
|**404**|Account is not present in database|string|


### Deposit money to account
```
POST /account/deposit
```


#### Description
Deposits money into an account, returns current balance


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Deposit was completed successfully|BigDecimal|
|**404**|Account is not present in database|string|
|**406**|Illegal amount money to process|string|


### Withdraw money from account
```
POST /account/withdraw
```


#### Description
Withdraws money from account, returns current balance


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Withdraw was completed successfully|BigDecimal|
|**400**|Account has no sufficient funds to withdraw|string|
|**404**|Account is not present in database|string|
|**406**|Illegal amount money to process|string|


## Definitions

<a name="user"></a>
### User

|Name|Schema|
|---|---|
|**username**  <br>*required*|string|
|**password**  <br>*required*|string|


<a name="transaction"></a>
### Transaction

|Name|Schema|
|---|---|
|**amount**  <br>*required*|BigDecimal|
|**accountId**  <br>*required*|uuid|
