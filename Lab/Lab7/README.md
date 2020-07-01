# 95-702 Distributed Systems for ISM                         
# HTTP REST Lab exercise

The purpose of this lab is to teach RESTful design and RESTful programming
in  Java. After this lab is complete, the student will be able to build RESTful
clients and servers in Java - using standalone Java or JAX-RS.

JAX-RS is the Java API for RESTful Web Services. It is a Java programming language
API specification that provides support in creating web services according to the Representational State Transfer (REST) architectural pattern. JAX-RS uses annotations, introduced in Java SE 5, to simplify the development and deployment of web service clients and endpoints.

This lab has three parts. The first part is the checkpoint. The second part
is a REST example that does not use JAX-RS on the server side. The third part is
a REST example that does use JAX-RS on the server side.

In this lab, you will compile and execute clients and servers that are designed to
interact by passing messages using HTTP. You will be asked to extend the clients and servers
to provide additional features. The clients will use a proxy design and will use REST.

# Part 1.

The first task is to get a client and a server running in IntelliJ using TomEE.
To create the client, create a standard Java project named WebServiceDesignStyles3ProjectClientLab.
Within that project, use the client side code provided in WebServiceDesignStyles3LabClient.java.
You may use your own package name. This client is standard Java.

To create the server side code, create a Java Web Application - much like you did in the first lab.
Use the server side code provided in VariableMemory.java. Name the server side project
WebServiceDesignStyles3ProjectServerLab.

Note: In the Run/Debug configurations (when you are configuring TomEE) change the
URL to http://localhost:8080/WebServiceDesignStyles3ProjectServerLab/VariableMemory/

Note: Under Run/Debug Configurations/Deployment change the Application Context to
/WebServiceDesignStyles3ProjectServerLab/VariableMemory

Use the web.xml provided. Be sure to use your own Andrew ID in place of "mm6".

Deploy the server side project to TomEE. Run the client side code. Spend some time studying
both the client and the server.

Show your TA that you have the client and server running.

:checkered_flag:**This is the checkpoint for this lab.**

# Part 2.

The second task is to modify the client so that it provides methods called getVariableList() and
the lower level method named doGetList(). Note the call to the getVariableList method (commented out)
within the client side code. After modifying the server, remove the comment symbols from this
call. That is, the method should actually work.

The getVariableList method has the following signature and description:
```
public static String getVariableList()
// makes a call to doGetList()
// returns a list of all variable defined on the server.
```
The doGetList method has the following signature and description:
```
public static int doGetList(Result r)
// Makes an HTTP GET request to the server. This is similar to the doGet provided on the client
// but this one uses a different URL.
// This method makes a call to the HTTP GET method using
// http://localhost:8080/WebServiceDesignStyles3ProjectServerLab/VariableMemory/"
```

You will also need to modify the doGet method on the server. Currently it returns
an HTTP 401 if a name is not provided in the URL. Your new doGet method will return
a list of variable names found in the map. This list of variable names will be returned
to the client for display. Clients will now be able to use two different URL's with a GET
request. One will be used to return the value of a variable and the other will be able
display the list of variables found within the map on the server. You are not being asked
to return the values that are also stored in the map.

For Part 2, show your TA your working solution. My solution has the following
output on the client side:
```
Begin main of REST lab.
Assign 100 to the variable named x
Assign 199 to the variable named x
Sending a GET request for x
199
Sending a DELETE request for x
x is deleted but let's try to read it
Error from server 401
It is interesting to use HTTP
in an RPC
kind of way. Welcome to REST!
abc
End main of REST lab
```

# Part 3.

In Part 3 you will use JAX-RS to write your server side code.

Recall the client and server code that you wrote in the RMI programming lab.
Your task in Part 3 is to do the same programming but using REST rather than
Java RMI. We are interested in comparing the speed of the REST approach and the
Java RMI approach. Which one is faster and by how much?

Usain Bolt is one of the fastest human runners. He is able to run 100 metres
in 9.58 seconds. A cheetah has been clocked running the same distance in 5.95
seconds. How much faster is the Cheetah? The Cheetah is 9.58 / 5.95 = 1.6 times
as fast as Bolt.

In this part of the lab, we want to know which is faster - Java RMI or REST and by
how much.

Review the Java RMI lab and use the same timing approach to time the RESTful
code provided here. Like the Java RMI lab, you will increment the counter 10,000
times.

Before the loop in the main routine of the client begins, set a timer like this:
```
long start = System.currentTimeMillis();
```
When the loop is finished, find the ending time like this:
```
long stop = System.currentTimeMillis();
```
Show your TA that you know which approach (Java RMI or REST) is faster and by how much.

Since this is the first time you have been exposed to JAX-RS, working client
and server code is provided.

See Bumper.java and BumperClient.java.

On the server side, you will need to do the following:

Create a new project. On the wizard, in the left-hand pane,
select **Java Enterprise**. In the Additional libraries and in the frameworks area,
select the **Web Application** and **RESTful Web Service** checkboxes.

Specify JDK, JavaEE and Application Server version. Use JDK12, JavaEE8 and TomEE.
Choose the **Download** option in the area below the Additional Libraries and Frameworks list.
Click Next.


From the Run menu, select Edit Configuration.
Under the Deployment tab, change the Application Context to /.
Also make sure a war artifact is listed.
Click OK to save the changes.
Start the Tom EE server.
