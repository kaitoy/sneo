SNeO
====

SNeO is an SNMP Network Simulator.
It is now available on the Maven Central Repository. 

* [SNeO 1.2.2](http://search.maven.org/#search|ga|1|sneo)

SNeO is powered by Pcap4J: https://github.com/kaitoy/pcap4j

Giane
-----

Giane is the Web GUI for SNeO.
Download giane-jetty.war from [here](http://search.maven.org/#search|ga|1|a%3A%22giane-jetty%22) and execute the following command with JDK 7:

`java -jar giane-jetty.war --httpPort 8080 --jmx.httpPort 8082 --jmx.rmiPort 1099`

After a while, you'll see a message `** Hit Enter key to stop Giane **`, which means Giane has been started.
Then, open a Web browser, access to `http://localhost:8080/giane/` and play with Giane.

License
-------

SNeO is distributed under the MIT license.

    Copyright (c) 2011-2014 Kaito Yamada
    All rights reserved.

    Permission is hereby granted, free of charge, to any person obtaining a copy of
    this software and associated documentation files (the "Software"), to deal in the Software without restriction,
    including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
    and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
    subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
    NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
    IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
    WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

