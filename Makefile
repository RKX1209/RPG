OBJS	=	*.java
JAVAC	=	javac
JAVA	=	java
MAKE	= 	make

install : 
	$(MAKE) jar

jar	: *.class Makefile

%.class : %.java Makefile
	$(JAVAC) $*.java
