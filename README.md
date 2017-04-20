# Secure Storage with GUI

* Author : Aditya Manikashetti - 1001819
* Author : Tenzin Chan - 1001522
* Date: 20/04/2017 

***

## Network Assignment 1

![alt text](https://raw.githubusercontent.com/mm-aditya/SecureStorageGUI/readme_resources/DiagramForHandshake.png)

**Purpose**  
To implement a secure file upload application from a client to an Internet file server. Secure concentrates on two properties: *authenticating the ID of the file server to prevent leakage of data to unauthorized sources* and *protecting the confidentiality during upload*. 
  
**Compilation**  
> In the works.

**Working**  
> Start SecStore first to allow it to listen on port 6789.
> Client will send a nonce (50-digit BigInteger) to the IP address entered.
> SecStore encrypts nonce with its private key and sends it back to Client.
> Client requests for server certificate, and uses public key in certificate to decrypt the encrypted nonce. If it is the same as the
> nonce sent, Client proceeds to encrypt the file with SecStore's public key and sends it. If it is not, connection is closed.
>
> For CP2, once the SecStore is verified, Client will request for a symmetric key. SecStore will send it over and Client will encrypt
> the file with the symmetric key using AES before sending it to SecStore.
