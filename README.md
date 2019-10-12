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

### Endpoints

| URL | Method | Request Body | Expected Response | Description |
| :--- | :--- | :--- | :--- | :--- |
| /api/auth/v1/authenticate | POST | username, password | Session Object | Checks the database for a matching username & password combination |
| /api/auth/v1/checkSession | POST | sessionToken, requestNumber | Boolean | Checks whether the requested session is still active in the system |
| /api/auth/v1/logout | POST | sessionToken, requestNumber | HttpStatus Object | Ends the requested session |
| /api/auth/v1/users/select/all |  POST  | sessionToken, requestNumber | Array of User Objects | Returns all user information currently stored in the database |
| /api/auth/v1/users/select/{username} | POST | sessionToken, requestNumber  | User Object | Returns specific user based on {username} path variable |
| /api/auth/v1/users/insert | POST | sessionToken, requestNumber, username, email, password | HttpStatus Object | Inserts a user into the database |
| /api/auth/v1/users/delete | POST | sessionToken, requestNumber, username | HttpStatus Object | Removes a user from the database |
| /api/auth/v1/users/update | POST | sessionToken, requestNumber, user_id, username, email, password | HttpStatus Object | Updates a user in the database |
| /api/auth/v1/groups/select/all | POST | sessionToken, requestNumber | Array of Group Objects | Returns all groups currently stored in the database |
| /api/auth/v1/groups/insert | POST | sessionToken, requestNumber, groupName | HttpStatus Object | Inserts a group into the database |
| /api/auth/v1/groups/delete | POST | sessionToken, requestNumber, groupName | HttpStatus Object | Removes a group from the database |
| /api/auth/v1/sessions/select/all | POST | sessionToken, requestNumber | Array of Session Objects | Returns all sessions currently active in the system |

___

### Contact & Contributing

You can currently reach me at ***beiler.coleman@gmail.com***. <br />
I've got a lot of ideas around security and I would love to hear your's too. <br />
Feel free to reach out for any support / questions.





















