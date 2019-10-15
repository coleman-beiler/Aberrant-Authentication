## Aberrant-Authentication
Something aberrant has wandered away from the usual path or form. 
The word is generally used in a negative way; aberrant behavior, 
for example, may be a symptom of other problems. 
But the discovery of an aberrant variety of a species can be exciting
news to a biologist, and identifying an aberrant gene has led the way
to new treatments for diseases. <br />

<p align="center">
    <img alt="aberrant logo" src="./NOTES/ABERRANT2.png" />
</p>

### Description
Aberrant aims to make session tracking and user authentication a breeze.
Within minutes, you can have a fully secure solution to a tough problem.
Sure, authentication / authorization services are everywhere:
* [Okta](https://www.okta.com/)
* [Firebase](https://firebase.google.com/products/auth/)
* [OneLogin](https://www.onelogin.com/)
* [Duo](https://duo.com/)

###### So what makes Aberrant different?<br />
Well, the biggest selling point of using Aberrant is that the base services and features are free.<br />
It requires no sign-up, it doesn't track your usage, and it doesn't require internet access to work.<br />
It will continue to receive security updates for the lifetime of the project.<br />

I believe that access control, session tracking, and authentication should be the <b><u>first</u></b> thing
done when creating an application. Security doesn't have to be an after-thought anymore; even if you aren't developing online.

___

### Deployment Instructions
Aberrant is easy and only requires that you have Java 8 installed and have a running database that is 
[compatible with liquibase](http://www.liquibase.org/databases.html). 

1. Clone the repository to your preferred location<br/> ```git clone https://github.com/Colemayne/Aberrant-Authentication.git```
2. Edit the application.properties file with the following:

    + server.port : Which port you'd like Aberrant to operate on
    + spring.datasource.url : `jdbc:database:address&port/databaseName`
    + spring.datasource.username : database user trusted to manage the tables
    + spring.datasource.password : database user's password (Please make sure this is changed and secure).
  
3.  Run the following bash commands from the top level directory of the project:

    + `./gradlew clean`
    + `./gradlew compileApp`
    + `java -jar build/services/Aberrant-Authentication-Web-API-0.0.1.jar`
    
That's it on the deployment! There is a little bit more you should know though.

* The default user is 'admin' and it's password is 'admin' as well..  (Please change this immediately).
* I am currently working on a small Vue.js project that will allow you to interface with this API from the UI to make it easier on you.

___

### Example Usage

In this example, we will be using javascript to request information on a specific user. It's important to note that this example assumes you've already authenticated.

```javascript
  fetch('/api/auth/v1/users/select/user', {
    method: 'GET',
    headers: {
      'sessionToken': localStorage.sessionToken,
      'refreshToken': localStorage.refreshToken,
      'requestNumber': localStorage.requestNumber
    }
  }).then((result) => {
    localStorage.refreshToken = result.headers.get("refreshtoken");
    return result.json();
  }).then((data) => {
    let user = data[0];
    localStorage.requestNumber++;
    console.log("Found the user: "+user);
  }).catch((error) => {
    console.error(error);
  });
```
___

### Endpoints

URL: /api/auth/v1/authenticate
  * Method: POST
  * Request Body: username, password
  * Request Headers: N/A
  * Response Body: Session Object
  * Response Headers: N/A
  * Description: Checks the database for a matching username & password combination and returns a session to the client
  ***
URL: /api/auth/v1/checkSession
  * Method: POST
  * Request Body: sessionToken, refreshToken, requestNumber
  * Request Headers: N/A
  * Response Body: sessionResponse Object ('answer' -> Boolean, 'refreshToken' -> String)
  * Response Headers: N/A
  * Description: Checks whether the requested session is still active in the system
  ***
URL: /api/auth/v1/logout
  * Method: POST
  * Request Body: sessionToken, refreshToken, requestNumber
  * Request Headers: N/A
  * Response Body: N/A
  * Response Headers: N/A
  * Description: Ends the requested session
  ***
URL: /api/auth/v1/users/select, /api/auth/v1/users/select/{username}
  * Method: GET
  * Request Body: N/A
  * Request Headers: sessionToken, refreshToken requestNumber
  * Response Body: Array of User Objects
  * Response Headers: refreshToken
  * Description: Returns all user information currently stored in the database unless a username is specified
  ***
URL: /api/auth/v1/users/insert
  * Method: POST
  * Request Body: sessionToken, refreshToken, requestNumber, username, email, password
  * Request Headers: N/A
  * Response Body: N/A
  * Response Headers: refreshToken
  * Description:  Inserts a user into the database
  ***
URL: /api/auth/v1/users/delete 
  * Method: POST
  * Request Body: sessionToken, refreshToken, requestNumber, username
  * Request Headers: N/A
  * Response Body: N/A
  * Response Headers: refreshToken
  * Description: Removes a user from the database
  ***
URL: /api/auth/v1/users/update
  * Method: POST
  * Request Body: sessionToken, refreshToken, requestNumber, user_id, username, email, password
  * Request Headers: N/A
  * Response Body: N/A
  * Response Headers: refreshToken
  * Description: Updates a user currently in the database
  ***
URL: /api/auth/v1/groups/select, /api/auth/v1/groups/select/{groupName}
  * Method: GET
  * Request Body: N/A
  * Request Headers: sessionToken, refreshToken, requestNumber
  * Response Body: Array of Group Objects
  * Response Headers: refreshToken
  * Description: Returns all group information currently stored in the database unless a group name is specified
  ***
URL: /api/auth/v1/groups/insert 
  * Method: POST
  * Request Body: sessionToken, refreshToken, requestNumber, groupName
  * Request Headers: N/A
  * Response Body: N/A
  * Response Headers: refreshToken
  * Description: Inserts a group into the database
  ***
URL: /api/auth/v1/groups/delete
  * Method: POST
  * Request Body: sessionToken, refreshToken, requestNumber, groupName
  * Request Headers: N/A
  * Response Body: N/A
  * Response Headers: refreshToken
  * Description: removes a group from the database
  ***
URL: /api/auth/v1/sessions/select/all
  * Method: GET
  * Request Body: N/A
  * Request Headers: sessionToken, refreshToken, requestNumber
  * Response Body: Array of Session Objects
  * Response Headers: refreshToken
  * Description: Returns all sessions currently active in the system

___

### Contact & Contributing

You can currently reach me at ***beiler.coleman@gmail.com***. <br />
I've got a lot of ideas around security and I would love to hear your's too. <br />
Feel free to reach out for any support / questions.





















