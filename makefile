JCC = javac
JAVA = java

JFLAGS = -g

CLASSPATH = ./bin

CLIENTPATH = ./src/client
SERVERPATH = ./src/server

default: ClientAll

# Compiling

ClientAll:
		$(JCC) $(JFLAGS) -d $(CLASSPATH) $(CLIENTPATH)/*.java

ServerAll:
		$(JCC) $(JFLAGS) -d $(CLASSPATH) $(SERVERPATH)/*.java

all: ClientAll ServerAll

# Running
runc:
		$(JAVA) -cp $(CLASSPATH) client.Main_Client

runs:
		$(JAVA) -cp $(CLASSPATH) server.Main_Server start

clean: 
		$(RM) $(CLASSPATH)/server/*.class
		$(RM) $(CLASSPATH)/client/*.class
