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
To perform any authenticated or authorized request you should provide your Facebook Token to the next endpoint:

>POST /authenticate
{
    "facebookToken": "EAAHC..."
}

### Implemented API
Implemented API

### API to implement in future
API to implement in future

## Setting-up the project
Setting-up the project

