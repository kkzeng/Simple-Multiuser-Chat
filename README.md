# Simple-Multiuser-Chat
A simple multiuser chat with chat logs written in Java with a simple Swing GUI.
I used this project in order to learn more about the concepts of sockets and multithreading in Java.

I guess if someone wants to chat with their friends without using Facebook, Google Hangouts, Snapchat or the multitude of services available to them on the web then they could use this as some sort of hipster retro kind of chat client - maybe reminiscent of AIM or MSN Messenger. 

So just in case someone wants to use it, I'll leave some instructions. 

Instructions For Use:
1. Run the ChatServer.java (make sure port 9000 is free, or change the source code to whichever port you want).
2. Run the ChatClient.java for each user as needed.
3. Enter the IP address "localhost" or "127.0.0.1" or if you're hosting the ChatServer on a remote server, use that IP.
4. Enter a unique name (if name already exists, program will prompt you to write another name).

Additional Info:
Chat logs are saved in the server computer's home directory (System.getProperty("user.home"))
