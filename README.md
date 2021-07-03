# BSA-Spring-Security

Spring Boot Application Development, implementing the required functionality.

## What is done: 

1. The registration functionality has been implemented. The user has the opportunity to register by sending the appropriate
   a request to the backend, or using OAuth2 if its account has not been created.
2. The functionality of a refresh token has been implemented: when a user is authorized, he is given 2 keys with different lifetimes.
   The first is 30 minutes (access_token), the second is 30 days - refresh_token. Created endpoint accepting refresh_token in
   body of the request and returning a new pair of access and refresh tokens.
3.Created OAuth2 test apps for [Google](https://console.developers.google.com/) and [Github](https://github.com/settings/developers).
4. Made so that only the owner of the hotel or the administrator can change the description of the hotel, as well as delete it.
5. The ability to change the password has been implemented.
6. Password reset of the "Forgot password" type has been implemented. The flow is as follows:
    1. A request is sent with the user's mail to the endpoint. For simplicity, you do not need to send anything by mail: in
       the console displays a token that will later be used to reset the password.
    2. A request with a new password and token is sent to the server, if everything is ok, the password changes.
7. Spring Boot Actuator is connected and only administrators have access to it.
