# Makefile for Cockatoo
# (c) Chris Samuel 2012
# License: GPLv3. http://geekscape.org/static/parrot_license.html

all:	compile

compile: Cockatoo.java  Display.java  KeyboardInput.java  ParrotCommunication.java  Server.java
	javac Cockatoo.java

run: compile
	java Cockatoo

clean:
	@rm -fv *.class
