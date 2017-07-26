@echo off
setlocal

java -Dorg.sneo.tools.console.snmpRequest.VariableTextFormat=org.sneo.util.ColonSeparatedOidTypeValueVariableTextFormat -cp log4j.jar;slf4j-log4j12;slf4j-api.jar;snmp4j.jar;pcap4j-core.jar;sneo-core.jar com.github.kaitoy.sneo.tools.console.SneoSnmpRequest -Ow -v 2c -c %1 %2 %3

rem SNMPv3
rem java -Dorg.sneo.tools.console.snmpRequest.VariableTextFormat=org.sneo.util.ColonSeparatedOidTypeValueVariableTextFormat -cp log4j.jar;slf4j-log4j12;slf4j-api.jar;snmp4j.jar;pcap4j-core.jar;sneo-core.jar com.github.kaitoy.sneo.tools.console.SneoSnmpReques -Ow -v 3 -a SHA -A password -l authPriv -n config -u public -x DES -X password %1 %2

