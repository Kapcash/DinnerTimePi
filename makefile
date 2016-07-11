JCC = javac
JAVA = java
JAR = jar

JFLAGS = -g
JARFLAGS = -cvfm

CLASSPATH = ./bin

CLIENTPATH = ./src/client
SERVERPATH = ./src/server

default: ClientAll

# Compiling

ClientAll:
		$(JCC) $(JFLAGS) -d $(CLASSPATH) $(CLIENTPATH)/*.java
		$(JAR) $(JARFLAGS) DinnerTimeClient.jar bin/client/MANIFEST.MF bin/client/*.class data/
ServerAll:
		$(JCC) $(JFLAGS) -d $(CLASSPATH) $(SERVERPATH)/*.java
		$(JAR) $(JARFLAGS) DinnerTimeServer.jar bin/server/MANIFEST.MF bin/server/*.class

all: ClientAll ServerAll

# Running
runc:
		$(JAVA) -jar DinnerTimeClient.jar

runs:
		$(JAVA) -jar DinnerTimeServer.jar

clean: 
		$(RM) $(CLASSPATH)/server/*.class
		$(RM) $(CLASSPATH)/client/*.class
