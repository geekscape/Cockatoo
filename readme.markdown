Cockatoo AR.Drone program
=========================

Code author: Andy Gelme

Documentation author: Chris Samuel based on what I can gather from the source. :-)

License: GPLv3. http://geekscape.org/static/parrot_license.html

Contents
--------
- [Introduction](#introduction)
- [Installation](#installation)
- [Usage](#usage)
- [Feedback and issues](#feedback)

<a name="introduction" />
Introduction
------------
The [AR.Drone Parrot](http://ardrone.parrot.com)
is an off-the-shelf, low-cost hobbyist quadcopter which is typically flown via
an Android phone, iPhone or other iOS device over WiFi.  The AR.Drone Parrot
acts as a WiFi Access Point allowing direct connection from a laptop or mobile
device.

The AR.Drone Parrot provides various UDP and TCP ports using a documented
protocol.  See "ARDrone Developer Guide, SDK 1.5, 2010-10-05, chapters 6 and 7".

Cockatoo is a Java service that implements the AR.Drone Parrot protocol.
Cockatoo can act as a bridge between various forms of user input, such as a
keyboard or even a Kinect for gesture control.

[Video of Cockatoo being using inconjuction with a Kinect client developed by
Jon Oxer](http://www.youtube.com/watch?v=mREorv0hbY8) and his
[LCA2011 presentation](http://www.youtube.com/watch?v=yyYu4h7ZXF8).

<a name="installation" />
Installation
------------
To compile:

      $ make

To run:

      $ make run

To clean:

      $ make clean

<a name="usage" />
Usage
-----
The Cockatoo proxy connects to an AR.Drone 1 (currently) on the default
IP address of 192.168.1.1 and sends it UDP commands to control it.

When the proxy starts it sets up a TCP port listening for connections on
5600 and accepts commands in plain text.

Currently those commands are:

- takeoff
- land
- forward <repeat>
- backward (possibly with a <repeat> argument)

It also opens an GUI window which listens for key presses and translates
them into commands for the drone.  These are:

- t - takeoff
- l - land
- q - land
- f - flat trim
- e - emergency (the source says "reset")
- h - hover (also the space bar)
- 0-9 - rate
- "up" - climb
- "shift-up" - forwards
- "down" - descend
- "shift down" - backwards
- "left" - rotate left (yaw left)
- "shift left" - roll left
- "right" - rotate right (yaw right)
- "shift right" - roll right

<a name="feedback" />
Feedback and issues
-------------------
Tracking is managed via GitHub ...

- [Enhancements requests and issue tracking](https://github.com/geekscape/Cockatoo/issues)
