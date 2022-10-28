# Oneweek - Interview Planning Application

## Overview
* [Project information](#project-information)
  * [Used technologies](#used-technologies)
* [API](#api)
  * [Authentication & authorization](#authentication--authorization)
  * [Implemented API](#implemented-api)
  * [API to implement in future](#api-to-implement-in-future)
* [Setting-up the project](#setting-up-the-project)

## Project information
Interview planning application is a RESTful service designed for better communication between interviewers and candidates through coordinators.

Application supports next basic functionality:
  - Creating slots as Interviewer
  - Creating slots as Candidate
  - Creating bookings for already created Interviewer and Candidate slots as Coordinator

### Used technologies
- Java
- Spring
  - Boot
  - Data
    - Hibernate
  - Security
    - OAuth2
  - Web
- Facebook API

## API
This section describes all implemented and planned to implement endpoints.

### Authentication & authorization

#### Getting the JWT

To perform any authenticated or authorized request you should provide your Facebook Token to the next endpoint:

`POST /authenticate`

With requiered data parameter `{"facebookToken": "EAAHC..."}`.

As the response you will get JSON Web Token as `{"token": "eyJhb..."}`.

The possible exceptions are:
- 401 - bad_facebook_token_exception
- 401 - bad_credentials

Gained JWT should be put in request header as a parameter `Authorization` with value `Bearer eyJhb...`.

#### Edge cases

You can face next exceptions while using JWT within yout request:
- 401 - not_authenticated_exception 
- 401 - bad_token_exception
- 401 - expired_token_exception
- 401 - bad_token_signature_exception
- 401 - malformed_token_exception
- 401 - unsupported_token_exception
- 403 - access_denied_exception

#### Users
There are four groups of users:
- Guests - users without authentication
- Candidates - users that have passed authentication but have not Interviewer or Coordinator role
- Interviewers - users that have passed authentication and have Interviewer role
- Сoordinator - users that have passed authentication and have Coordinator role

### Implemented API
Implemented API

### API to implement in future
API to implement in future

## Setting-up the project
Setting-up the project

