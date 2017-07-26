SNeO
====

SNeO is an SNMP Network Simulator.
SneO runs SNMP agents that loads MIB instances from human-readable text files (Text MIBs), ICMP nodes, and a network between the agents and nodes.

Download
--------

* [SNeO 1.2.3](http://search.maven.org/#search|ga|1|sneo)

Features
--------

SNeO has the following features:

* Multi-platform. (e.g. Windows, CentOS, Solaris (SPARC, x86), HP-UX (IPF), etc.)
* SNMP agent simulation (powered by [SNMP4J-Agent](http://www.snmp4j.org/))
    * SNMPv1/v2c/v3
    * IPv4/v6
    * GET, GETNEXT, SET, and GETBULK
    * Dynamic sysUpTime
    * Sending coldStart trap
    * SNMP community string indexing
    * JMX
        * to get agent information
        * to stop/start agents
        * to change the log level
        * to get/set MIB instances
        * to reload Text MIBs
        * to get SNMP access statistics
* Network simulation (powered by [Pcap4J](https://github.com/kaitoy/pcap4j))
    * IPv4/v6
    * ICMPv4/v6
    * Node with multi IP address
    * VLAN
    * Link aggregation
* Sending SNMP request (powered by [SNMP4J](http://www.snmp4j.org/))
    * SNMPv1/v2c/v3
    * GET, GETNEXT, SET, and GETBULK
    * Walking an SNMP agent and generate a Text MIB

Build
-----

Git, Maven 3, and JDK 1.8 are required to be properly installed.

```cmd
$ git clone https://github.com/kaitoy/sneo.git
$ cd sneo
$ mvn install
```

How to Use
----------

### Preparation

Maven 3 and JDK 1.8 are required to be properly installed.

The following command downloads dependencies and copies the SNeO core artifact from the target directory.

```cmd
$ cd sneo-core/bin
$ ./prepare.sh
```

### Generating Text MIBs

JRE 1.8 is required to be properly installed.

SNeO snmpwalks on a device and generate a Text MIB by the following command.

```cmd
$ cd sneo-core/bin
$ ./walk.sh {community string} {target hostname} . 1> {Text MIB}
```

Alternatively, you can use `snmpwalk` command of net-snmp-utils.

```cmd
$ snmpwalk -M /dev/null -On -Oe -OU -c {community string} -v 2c {target hostname} . 1> {Text MIB}
```

### Simulating an SNMP agent

JRE 1.8 is required to be properly installed.

```cmd
$ cd sneo-core/bin
$ ./runAgent.sh {Text MIB} {agent IP address} {community string}
```

By the above command, SNeO starts to listen at the specified IP address and port 161.
It's needed that the IP address is added to an interface before starting simulation.

### Simulating multiple SNMP agents at once

JRE 1.8 is required to be properly installed.

### Simulating a network node

JRE 1.8, a pcap library (e.g. libpcap, WinPcap, and Npcap) are required to be properly installed.

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

    Copyright (c) 2011-2017 Kaito Yamada
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
