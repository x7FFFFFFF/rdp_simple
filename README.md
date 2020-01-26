# rdp_simple
Remote desktop tool 

## Requirements:
Java 8 or more
## Usage:
1. mvn package
1. #### On remote computer:
   >  java -Dp1=<port1> -Dp2=<port2> -jar rdp-0.1.jar
   #### On local computer:
   >  java -Dhost=<IPV4_REMOTE_HOST> -Dp1=<port1> -Dp2=<port2> -jar rdp-0.1.jar
 
 
        
