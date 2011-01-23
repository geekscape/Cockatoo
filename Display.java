/* Display.java
 * ~~~~~~~~~~~~
 * Please do not remove the following notices.
 * Copyright (c) 2010 by Geekscape Pty. Ltd.
 * License: GPLv3. http://geekscape.org/static/parrot_license.html
 *
 * To Do
 * ~~~~~
 * - Implement Swing GUI (review iPhone UI).
 * - Provide emergency button.
 * - Protocol reset.
 * - Flat trim.
 * - Take-off and landing buttons.
 * - Display navigation.
 *   - Link and flight error messages.
 *   - Battery level meter.
 * - Log console with filtering.
 * - Configuration.
 *
 * - Video ?
 */

import javax.swing.*;

public class Display extends JFrame {

  public Display(
    KeyboardInput keyboardInput) {

    addKeyListener(keyboardInput);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(320, 320);
    setVisible(true);
  }
}
