## Persitence

We have chosen to use implicit saving in our app. The reason for this is due to the fact that users are not supposed to have access to the file in which the user information is stored. 

When signing up, a map is generated with the users username, email, password (in which is automatically hashed) and a auto-generated userID. The password is hashed due to security measurements. The UserID is generated to further communicate with the API i the upcoming release, and to connect the currently logged in user to his/her profile. All user info is stored in JSON format, and located in moosemate/persistence/src/main/resources/data.json. 

MooseMate is currently designed to work as a desktop application, and runs on both Windows and MacOS. 