/* KeyboardInput.java
 * ~~~~~~~~~~~~~~~~~~
 * Please do not remove the following notices.
 * Copyright (c) 2010 by Geekscape Pty. Ltd.
 * License: GPLv3. http://geekscape.org/static/parrot_license.html
 *
 * To Do
 * ~~~~~
 * - Use higher-level Parrot communication abstractions.
 * - Continual (smoothly) rotate left and right.
 * - Move (smoothly) up and down 10 cm jumps.
 * - Check smoothness of all commands ...
 *   - Roll, pitch, vertical, rotation.
 */

import java.awt.event.*;

public class KeyboardInput implements KeyListener {
  private static float RATE_SLOW = 3.0f;      // roll and pitch less aggressive

  private static float RATE_TABLE[] = {
    0.99f, 0.05f, 0.10f, 0.20f, 0.30f, 0.40f, 0.50f, 0.60f, 0.70f, 0.85f
  };

  private float rate = RATE_TABLE[4];

  private boolean shiftPressed = false;

  private ParrotCommunication parrotCommunication = null;

  public KeyboardInput(
    ParrotCommunication parrotCommunication) {

    this.parrotCommunication = parrotCommunication;
  }

  public void keyTyped(
    KeyEvent keyEvent) {
  }

  public void keyPressed(
    KeyEvent keyEvent) {

    int keyCode = keyEvent.getKeyCode();

//  System.out.println("KeyCode pressed: " + keyCode);

    switch (keyCode) {
      case KeyEvent.VK_SHIFT:
      shiftPressed = true;
      break;

      case KeyEvent.VK_SPACE:
      case KeyEvent.VK_H:
        parrotCommunication.emergencyAbort = true;

        parrotCommunication.transmitProgressiveCommand(
          ParrotCommunication.MODE_HOVER, 0f, 0f, 0f, 0f
        );
        break;

      case KeyEvent.VK_0:
      case KeyEvent.VK_1:
      case KeyEvent.VK_2:
      case KeyEvent.VK_3:
      case KeyEvent.VK_4:
      case KeyEvent.VK_5:
      case KeyEvent.VK_6:
      case KeyEvent.VK_7:
      case KeyEvent.VK_8:
      case KeyEvent.VK_9:
        rate = RATE_TABLE[(keyCode - KeyEvent.VK_0)];
//      System.out.println("rate: " + rate);
        break;

      case KeyEvent.VK_E:
        parrotCommunication.transmitRefCommand(                      // "reset"
          ParrotCommunication.BEHAVIOUR_EMERGENCY
        );
        break;

      case KeyEvent.VK_F:
        parrotCommunication.transmitCommand("FTRIM", null);
        break;

      case KeyEvent.VK_L:
        parrotCommunication.transmitRefCommand(
          ParrotCommunication.BEHAVIOUR_LAND
        );
        break;

      case KeyEvent.VK_Q:
        parrotCommunication.transmitRefCommand(
          ParrotCommunication.BEHAVIOUR_LAND
        );
        System.exit(0);
        break;

      case KeyEvent.VK_T:
        parrotCommunication.transmitRefCommand(
          ParrotCommunication.BEHAVIOUR_TAKEOFF
        );
        break;

      case KeyEvent.VK_UP:
        if (shiftPressed) {
          parrotCommunication.transmitProgressiveCommand(                 // Up
            ParrotCommunication.MODE_PROGRESSIVE, 0f, 0f, rate, 0f
          );
        }
        else {
          parrotCommunication.transmitProgressiveCommand(            // Forward
            ParrotCommunication.MODE_PROGRESSIVE, 0f, -rate / RATE_SLOW, 0f, 0f
          );
        }
        break;

      case KeyEvent.VK_DOWN:
        if (shiftPressed) {
          parrotCommunication.transmitProgressiveCommand(               // Down
            ParrotCommunication.MODE_PROGRESSIVE, 0f, 0f, -rate, 0f
          );
        }
        else {
          parrotCommunication.transmitProgressiveCommand(               // Back
            ParrotCommunication.MODE_PROGRESSIVE, 0f, rate / RATE_SLOW, 0f, 0f
          );
        }
        break;

      case KeyEvent.VK_LEFT:
        if (shiftPressed) {
          parrotCommunication.transmitProgressiveCommand(  // Yaw left (rotate)
            ParrotCommunication.MODE_PROGRESSIVE, 0f, 0f, 0f, -rate
          );
        }
        else {
          parrotCommunication.transmitProgressiveCommand(          // Roll left
            ParrotCommunication.MODE_PROGRESSIVE, -rate / RATE_SLOW, 0f, 0f, 0f
          );
        }
        break;

      case KeyEvent.VK_RIGHT:
        if (shiftPressed) {
          parrotCommunication.transmitProgressiveCommand( // Yaw right (rotate)
            ParrotCommunication.MODE_PROGRESSIVE, 0f, 0f, 0f, rate
          );
        }
        else {
          parrotCommunication.transmitProgressiveCommand(         // Roll right
            ParrotCommunication.MODE_PROGRESSIVE, rate / RATE_SLOW, 0f, 0f, 0f
          );
        }
        break;
    }
  }

  public void keyReleased(
    KeyEvent keyEvent) {

    int keyCode = keyEvent.getKeyCode();

    switch (keyCode) {
      case KeyEvent.VK_SHIFT:
      shiftPressed = false;
      break;
    }
  }
}
