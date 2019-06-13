# LibrarySystemMaven
This is simple Library System Application

Technologies used in this.

Akka Http
node
PM2 node application server
How to start

Java 8 is install on your machine node > 8.11 is install intellij IDM

clone this project load it in intellij update maven libraries jar

install node for MAC "https://www.webucator.com/how-to/how-install-nodejs-on-mac.cfm"

install pm2 http://pm2.keymetrics.io/

Now open LibraryServer.java and run this file in IDM

pm2 serve PATH-TO-THE-HTML-FILES-ROOT 3001

Now go to localhost:3001 It show you the home page. go to the localhost:3001/login.html

login to admin with admin@librarySys.com/admin@lib now can add users in the system.

Run LibraryServer.java file

Note: HTML files are in web folder at root level postman collection is also at root level to test API with postman
