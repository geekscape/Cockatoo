/* Cockatoo.java
 * ~~~~~~~~~~~~~
 * Please do not remove the following notices.
 * Copyright (c) 2010 by Geekscape Pty. Ltd.
 * License: GPLv3. http://geekscape.org/static/parrot_license.html
 * Version: 0.0
 *
 * Description
 * ~~~~~~~~~~~
 * ARDrone Parrot (quadcopter) GUI and proxy-server.
 *
 * To Do
 * ~~~~~
 * - Create parrot_license.html.
 * - Create readme.markdown, using notes.txt.
 *
 * - Logging slowing down responsiveness, use separate thread ?
 *
 * - Set-up Aiko-Gateway (Lua).
 *
 * - Create flight macros, can also be used by proxy-server.
 * - Support MobSenDat board input (over ZigBee, including GPS).
 *   - Waypoint recording and playback.
 *   - Fly predetermined course from a specified point.
 *
 * - Support Pebble board input / output (over ZigBee).
 */

import java.net.*;

public class Cockatoo {

  private Display display = null;

  private ParrotCommunication parrotCommunication = null;

  private Server server = null;

  public Cockatoo(
    InetAddress parrotAddress) {

    parrotCommunication = new ParrotCommunication(parrotAddress);

    display = new Display(new KeyboardInput(parrotCommunication));

    server = new Server(parrotCommunication);
  }

  public static void main(
    String args[]) {

    String parrotHostname = (args.length == 1)  ?
      args[0] : ParrotCommunication.DEFAULT_PARROT_HOSTNAME;

    try {
      Cockatoo cockatoo = new Cockatoo(InetAddress.getByName(parrotHostname));
    }
    catch (UnknownHostException unknownHostException) {
      System.err.println("Unknown host: " + parrotHostname);
      System.exit(-1);
    }
  }
}
