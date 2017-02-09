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
		$(JCC) $(JFLAGS) -d $(CLASSPATH) -cp $(CLASSPATH) $(COMMONPATH)/*.java

clientAll:
		$(JCC) $(JFLAGS) -d $(CLASSPATH)/ -cp $(CLASSPATH)/;$(LIBS) $(CLIENTPATH)/model/*.java $(CLIENTPATH)/view/*.java $(CLIENTPATH)/control/*.java
		$(JAR) $(JARFLAGS) DinnerTimeClient.jar $(CLASSPATH)/client/MANIFEST.MF $(CLASSPATH)/client/view/*.class $(CLASSPATH)/client/model/*.class $(CLASSPATH)/client/contorl/*.class $(CLASSPATH)/common/*.class

serverAll:
		$(JCC) $(JFLAGS) -d $(CLASSPATH) -cp $(CLASSPATH) $(SERVERPATH)/*.java
		$(JAR) $(JARFLAGS) DinnerTimeServer.jar $(CLASSPATH)/server/MANIFEST.MF $(CLASSPATH)/server/*.class $(CLASSPATH)/common/*.class

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
