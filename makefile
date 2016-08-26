JCC = javac
JAVA = java
JAR = jar

JFLAGS = -g
JARFLAGS = -cvfm

CLASSPATH = ./bin
LIBS = ./libs/swingx.jar

CLIENTPATH = ./src/client
SERVERPATH = ./src/server
COMMONPATH = ./src/common

default: all

# Compiling

common:
		$(JCC) $(JFLAGS) -cp $(CLASSPATH) $(COMMONPATH)/*.java

clientAll:
		$(JCC) $(JFLAGS) -d $(CLASSPATH) -cp $(CLASSPATH)\;$(LIBS) $(CLIENTPATH)/*.java
		$(JAR) $(JARFLAGS) DinnerTimeClient.jar bin/client/MANIFEST.MF bin/client/*.class bin/common/*.class

serverAll:
		$(JCC) $(JFLAGS) -d $(CLASSPATH) -cp $(CLASSPATH) $(SERVERPATH)/*.java
		$(JAR) $(JARFLAGS) DinnerTimeServer.jar bin/server/MANIFEST.MF bin/server/*.class bin/common/*.class

all: common clientAll serverAll

# Running

runc:
		$(JAVA) -jar DinnerTimeClient.jar

runs:
		$(JAVA) -jar DinnerTimeServer.jar

doc:
		javadoc -d ./data/doc -sourcepath ./src/ -private client server common

# Cleaning

clean: 
		$(RM) $(CLASSPATH)/server/*.class
		$(RM) $(CLASSPATH)/client/*.class
