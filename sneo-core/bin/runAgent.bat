@echo off

java -cp snmp4j.jar;snmp4j-agent.jar;log4j.jar;slf4j-log4j12;slf4j-api.jar;mx4j.jar;mx4j-tools.jar;pcap4j-core.jar;pcap4j-packetfactory-static.jar;jna.jar;sneo-core.jar;sneo-common.jar com.github.kaitoy.sneo.agent.SingleAgentRunner -f %1 -a %2 -c %3

rem Community String Indexing example
rem 
rem $ ls textMibs/
rem  mib  'mib@10'  'mib@20'
rem $ runAgent.sh textMibs/mib 192.168.0.100 public
rem Then, you can snmpwalk mib@10 by
rem $ walk.sh public@10 192.168.0.100 .1
rem 
rem java -cp snmp4j.jar;snmp4j-agent.jar;log4j.jar;slf4j-log4j12;slf4j-api.jar;mx4j.jar;mx4j-tools.jar;pcap4j-core.jar;pcap4j-packetfactory-static.jar;jna.jar;sneo-core.jar;sneo-common.jar com.github.kaitoy.sneo.agent.SingleAgentRunner -f %1 -a %2 -c %3 -csi 10 -csi 20
