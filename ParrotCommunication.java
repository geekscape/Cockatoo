/* ParrotCommunication.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~
 * Please do not remove the following notices.
 * Copyright (c) 2010 by Geekscape Pty. Ltd.
 * License: GPLv3. http://geekscape.org/static/parrot_license.html
 *
 * Description
 * ~~~~~~~~~~~
 * Implement Parrot communications protocol as described by ...
 * "ARDrone Developer Guide, SDK 1.5, 2010-10-05, chapters 6 and 7".
 *
 * To Do
 * ~~~~~
 * - Provide higher-level Parrot communiction abstractions.
 *   - Wrap transmitProgressiveCommand() with specific commands, e.g. roll.
 * - Navigation data-stream.
 *   - Monitor battery level.
 * - Retry DatagramSocket connection, if communications fails.
 * - Review "Developer Guide" configuration, chapter 8.3.
 * - Implement "flight animation" ...
 *   - pitch -30 deg: AT*ANIM=401,0,1000
 *   - pitch +30 deg: AT*ANIM=402,1,1000
 */

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class ParrotCommunication {

  public static final String DEFAULT_PARROT_HOSTNAME = "192.168.1.1";

  public static final int BEHAVIOUR_EMERGENCY = 290717952;    // TODO: Fix this
  public static final int BEHAVIOUR_LAND      = 290717696;    // TODO: Fix this
  public static final int BEHAVIOUR_TAKEOFF   = 290718208;    // TODO: Fix this

  private static final String CONTROL_LEVEL_BEGINNER = "\"0\"";
  private static final String CONTROL_LEVEL_ACE      = "\"1\"";
  private static final String CONTROL_LEVEL_MAX      = "\"2\"";

  public static final int MODE_HOVER       = 0;
  public static final int MODE_PROGRESSIVE = 1;

  private static final int PARROT_UDP_PORT_NAVIGATION = 5554;
  private static final int PARROT_UDP_PORT_VIDEO      = 5555;
  private static final int PARROT_UDP_PORT_COMMAND    = 5556;
  private static final int PARROT_TCP_PORT_CONTROL    = 5559;

  private static final int PORT_COMMAND_TIMEOUT = 3000;  // milliseconds

  private static final int COMMAND_DELAY_PERIOD = 50;  // milliseconds

  private static final int WATCHDOG_RESET_PERIOD = 200;  // milliseconds

  private InetAddress parrotAddress = null;

  private int sequenceNumber = 1;

  private StringBuffer lastTransmitBuffer = null;     // TODO: Still required ?

  private DatagramSocket socketCommand = null;

  public ParrotCommunication(
    InetAddress parrotAddress) {

//  System.out.println("Parrot IP address: " + parrotAddress);

    this.parrotAddress = parrotAddress;

    initialize();

    new WatchDogReset().start();

// TODO: Create proxy-server socket listener
  }

  private void initialize() {
    transmitCommand("PMODE",  "2");                        // misc. config data
    transmitCommand("MISC",   "2,20,2000,3000");
    transmitRefCommand(BEHAVIOUR_LAND);
    transmitCommand("COMWDG", null);                          // watchdog reset
    transmitCommand("CONFIG", "\"control:altitude_max\",\"2000\"");    // 2.0 m
    transmitCommand(
      "CONFIG", "\"control:control_level\"," + CONTROL_LEVEL_BEGINNER
    );
    transmitCommand("CONFIG", "\"general:navdata_demo\",\"TRUE\"");
//  transmitCommand("CONFIG", "\"general:video_enable\",\"TRUE\"");
    transmitCommand("CONFIG", "\"pic:ultrasound_freq\",\"8\"");
    transmitRefCommand(BEHAVIOUR_LAND);
    transmitProgressiveCommand(MODE_HOVER, 0f, 0f, 0f, 0f);
    transmitRefCommand(BEHAVIOUR_LAND);
    transmitCommand("FTRIM", null);
  }

  public void transmitProgressiveCommand(
    int   progressiveFlag,  //  0  (hover)         1  (progressive)
    float roll,             // -1  (left)      .. +1  (right)
    float pitch,            // -1  (forward)   .. +1  (backward)
    float verticalSpeed,    // -ve (down)      .. +ve (up)
    float angularSpeed) {   // -ve (spin left) .. +ve (spin right)

    transmitCommand("PCMD",
      progressiveFlag + "," +
      castFloatToInteger(roll) + "," +
      castFloatToInteger(pitch) + "," +
      castFloatToInteger(verticalSpeed) + "," +
      castFloatToInteger(angularSpeed)
    );
  }

  public void transmitRefCommand(
    int behaviour) {

    int refParameter = 0;

    transmitCommand("REF", new Integer(behaviour).toString());
//  transmitCommand("REF", castHexadecimalToInteger(refParameter));     // TODO
  }

  public void transmitCommand(
    String command,
    String arguments) {                                    // TODO: Use varargs

    StringBuffer buffer = new StringBuffer("AT*");
    buffer.append(command);
    buffer.append("=");
    buffer.append(incrementSequenceNumber());
    if (arguments != null) {
      buffer.append(",");
      buffer.append(arguments);
    }
    buffer.append("\r");

    transmit(buffer);

    lastTransmitBuffer = buffer;
  }

  private void transmit(
    StringBuffer buffer) {

//  System.out.println("transmit(): " + buffer);

    try {
      if (socketCommand == null) {
        socketCommand = new DatagramSocket(PARROT_UDP_PORT_COMMAND);
        socketCommand.setSoTimeout(PORT_COMMAND_TIMEOUT);
      }

      byte[] transmitBuffer = buffer.toString().getBytes();

      DatagramPacket datagramPacket = new DatagramPacket(
        transmitBuffer, transmitBuffer.length,
        parrotAddress,  PARROT_UDP_PORT_COMMAND
      );

      socketCommand.send(datagramPacket);
    }
    catch (IOException ioException) {
      System.err.println(
       "Can't initialize ARDone Parrot: " + ioException.getMessage()
      );
      System.exit(-1);
    }

    try {
      Thread.sleep(COMMAND_DELAY_PERIOD);               // TODO: This is dodgey
    }
    catch (InterruptedException interruptedException) {}
  }

  private synchronized int incrementSequenceNumber() {
    return(sequenceNumber ++);
  }

  private static int castFloatToInteger(
    float value) {

    ByteBuffer byteBuffer = ByteBuffer.allocate(4);

    byteBuffer.asFloatBuffer().put(0, value);
    return(byteBuffer.asIntBuffer().get(0));
  }

  class WatchDogReset extends Thread {
    public void run() {
      int lastSequenceNumber;

      while (true) {
        lastSequenceNumber = sequenceNumber;

        try {
          Thread.sleep(WATCHDOG_RESET_PERIOD);
        }
        catch (InterruptedException interruptedException) {}

        if (lastSequenceNumber == sequenceNumber) {
          transmitCommand("COMWDG", null);
//        transmit(lastTransmitBuffer);
        }
      }
    }
  }
}
