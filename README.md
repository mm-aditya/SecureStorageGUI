# GUI Network Assignment 1

* Author : Aditya Manikashetti - 1001819
* Author : Tenzin Chan - 1001522
* Date: 20/04/2017 

***

# Secure Storage with 

## Purpose 
To implement a secure file upload application from a client to an Internet file server. Secure concentrates on two properties: *authenticating the ID of the file server to prevent leakage of data to unauthorized sources* and *protecting the confidentiality during upload*. 
  
## Compilation 
Since we have a GUI you will have to include some dependencies while compiling and running our project. There is only one dependency file and it can be found under our readme_resources folder.  
> readme_resources/jfoenix.jar  
Once you have the dependencies included you need to run the following files in the *src/sample* directory: 
**Server**  
> javac Client.java  
**Clients**  
> javac Server.java  

## Usage  
Here's a guide on how to run the program:  
* Click on **Start Server** on the *server* window first.
* Then you may either enter the IPv4 address of the server into the field on the *client* window or use localhost.
* Click on **Handshake**. Wait for the server to authenticate. You can see the progress of authentication just below the progress bar.
* Once validated, enter the file name you want to transfer and the file name you want to save the transfered files as in the respective fields. If you have a file with the same name on the server **it will be overwritten**.
* Select which method of encryption you want to use and click **Upload**.
* The files that you uploaded should popup in the *server* window alongside which client uploaded it. You can upload files from multiple clients simultaneously.
* To exit client simply close the window. To exit server, you need to close the window and stop the server code from running in your console.

## Working  
* Start SecStore first to allow it to listen on port 6789.
* Client will send a nonce (50-digit BigInteger) to the IP address entered.
* SecStore encrypts nonce with its private key and sends it back to Client.
* Client requests for server certificate, and uses public key in certificate to decrypt the encrypted nonce. If it is the same as the
* nonce sent, Client proceeds to encrypt the file with SecStore's public key and sends it. If it is not, connection is closed.   
**Our fixed version of the Authentication Protocol**
![Image currently unavailable](https://raw.githubusercontent.com/mm-aditya/SecureStorageGUI/master/readme_resources/DiagramForHandshake.png)      

* For CP2, once the SecStore is verified, Client will request for a symmetric key. SecStore will send it over and Client will encrypt the file with the symmetric key using AES before sending it to SecStore.

**Data Collection**
We ran some test using the provided sample data and some of our own files. We used 3 file sizes and the average times of those file sizes across several comps as our input to plot a graph against.  
![Image currently unavailable](https://raw.githubusercontent.com/mm-aditya/SecureStorageGUI/master/readme_resources/graph.png)      
In addition to this we also tried some other file formats like Audio, Video and Images. The encryption times don't seem to change linearly with file size in some case as evident from the graph below.  
![Image currently unavailable](https://raw.githubusercontent.com/mm-aditya/SecureStorageGUI/master/readme_resources/Graph_withVideoImage.png)        

## Conclusion
In conclusion, this project was fun until the last few hours before submission when things started to break. Goodbye.



