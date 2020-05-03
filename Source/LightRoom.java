package Source;

import Source.Views.*;
import javax.swing.*;

public class LightRoom {
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    new LoginMenu();
  }
}