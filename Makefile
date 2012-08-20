# Makefile for Cockatoo
# (c) Chris Samuel 2012
# License: GPLv3. http://geekscape.org/static/parrot_license.html

all:	Cockatoo

Cockatoo: Cockatoo.java  Display.java  KeyboardInput.java  ParrotCommunication.java  Server.java
	javac Cockatoo.java

run: all
	java Cockatoo

clean:
	@rm -fv *.class
