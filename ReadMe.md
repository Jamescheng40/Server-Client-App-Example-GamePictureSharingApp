## General Info: The Following Requirement Should Be Met In Order To Have This APP Working

    1. Install MySQL DataBase to store image and user information.
    
    2. Install WebServer Hosting eg, IIS Express or Apache.
    
    3. PHP must be also installed in the computer.
    
Warning: above steps would set up user PC as a virtual server for this APP

## "Server-Client-App-Example-GamePictureSharingApp/WebFolder_PHP_Side" Contains All Neccessary Files For This Android APP

    1. Copy this folder into your WebServer Based Folder eg, Your IIS 7 Express should have an physical path in your actual disk drive
 ![alt text](https://raw.githubusercontent.com/Jamescheng40/Server-Client-App-Example-GamePictureSharingApp/master/imgForReadme/imag1.JPG)
    
## Info For Setting Up MySQL DataBase 

    1. If you don't know how to install, follow this link
 https://dev.mysql.com/doc/mysql-getting-started/en/
    
    2. The Port For DataBase Entry is 3306 and this port should not be Public by default.
    
    3. For testing purpose, username should be "root" and password should be "1234". You can change username and password later for the database if needed.
    
    4. You should set up a SCHEMAS called test_image and add Tables in this SCHEMAS for development and testing purposes.
       -DataBase should have the following Tables and Columns. Refer to the picture and code below for how to set up Tables.   
    
    
 


        

    
