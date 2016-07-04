JCC = javac
JAVA = java

JFLAGS = -g

CLASSPATH = ./bin

CLIENTPATH = ./src/client
SERVERPATH = ./src/server

default: ClientAll

# Compiling
Client:
		$(JCC) $(JFLAGS) -d $(CLASSPATH) $(CLIENTPATH)/ClientConnexion.java

ClientAll:
		$(JCC) $(JFLAGS) -d $(CLASSPATH) $(CLIENTPATH)/*.java

Server:
		$(JCC) $(JFLAGS) -d $(CLASSPATH) $(SERVERPATH)/ClientProcessor.java $(SERVERPATH)/TimeServer.java

ServerAll:
		$(JCC) $(JFLAGS) -d $(CLASSPATH) $(SERVERPATH)/*.java

all: ClientAll ServerAll

# Running
runc:
		$(JAVA) -cp $(CLASSPATH) client.Main_Client

runs:
		$(JAVA) -cp $(CLASSPATH) server.Main_Server

clean: 
		$(RM) $(CLASSPATH)/server/*.class
		$(RM) $(CLASSPATH)/client/*.class