# Promise POC

Welcome to Promise POC!

Promise POC is a proof of concept for the Promise Framework (https://github.com/assertdevelopments/promise). Promise is a JAX-RS extension that allows you to stream large as well as small streams of data in a memory efficient, time optimized and controlled way.

The goals of this proof of concept are:
- prove that the solution works and that the framework goals are realistic
- test the RESTfulness of the solution
- compare the differences between traditional JAX-RS serialization and the Promise Framework
- identify problems and blocking issues

Important Note: This proof of concept does not depend on JAX-RS. We chose to build a very low level POC (on top of servlets) so we can study the effects on the HTTP Protocol better.

The results of this proof of concept will be bundled in an article and will be used as guidelines for building the Promise Framework.

# Building

To build this project, you will need Maven 3 (or higher) and JDK 1.8.

Run the following command:

```
mvn clean package
```

# Usage

## Stream Servlet

The following code will create a stream servlet. When a client connects, the handleStreamRequest(Stream) method will be invoked.

``` java
@WebServlet(name = "ExampleStreamServlet", urlPatterns = "/example")
public class ExampleStreamServlet extends AbstractStreamServlet {

    @Override
    public void handleStreamRequest(Stream stream) throws Throwable {
    }

}
```

The servlet can now read/write bytes from/to the stream, using the *stream.getInputStream()* and *stream.getOutputStream()* methods.

What's the difference between an AbstractStreamServlet and a regular HttpServlet, you might wonder? HTTP/1.X requires you to write the Response status code (f.e. 200 "OK"), before sending the Response body. With a regular HttpServlet, you will be able to change your status code until your Response is committed (the outputstream is flushed for the first time or your outputstream buffer exceeds a certain amount of bytes). When trying to change the status code, after the response is committed, you will see the following error (log source WildFly):

```
12:33:50,463 ERROR [io.undertow.request] (default task-13) UT005023: Exception handling request to /example: java.lang.IllegalStateException: UT010019: Response already commited
```

When trying to write a very big body, you will always hit the buffer size. So you can no longer change the status code, after a certain amount of bytes. If an error occurs while writing the end of the body, the body will be corrupt, but status code 200 "OK" will still be returned.

The AbstractStreamServlet will solve this problem, by wrapping the body in an envelope. This envelope will return the real status code (500 "An unexpected error has occurred"), after sending the body contents. This allows us to send very big Response bodies, while still doing error handling.

When reading from the InputStream, the body will be read from the envelope and the real status code will be handled after receiving the body. 

## Stream Client

The following code will create a stream client. It will connect to the stream server and write the request body to the output stream. After all data is sent, the server response is being read from the server. 

``` java
StreamClient client = new StreamClient();
try {
    StreamResponse response = client.sendRequest(URI, outputStream -> {
        // write Request Body to the outputStream...
    });
    try {
        // read Response Body using response.getInputStream()...
    } finally {
        response.close();
    }
} finally {
    client.close();
}
```

The **StreamClient** will wrap the Request Body in an envelope, and will read the Response Body from the Response envelope.

# Samples

Download and install WildFly 10.

Deploy **promise-poc-c.v-SNAPSHOT.war** to the Wild Fly application server.

Try to run the samples in **org.assertdevelopments.promise.poc.samples.client** package.


# License

Copyright 2016 Assert Developments

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


