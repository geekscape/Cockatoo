# Makefile for Cockatoo
# (c) Chris Samuel 2012
# License: GPLv3. http://geekscape.org/static/parrot_license.html

all:	Cockatoo

Cockatoo: Cockatoo.java  Display.java  KeyboardInput.java  ParrotCommunication.java  Server.java
	javac Cockatoo.java

clean:
	@rm -fv *.class
