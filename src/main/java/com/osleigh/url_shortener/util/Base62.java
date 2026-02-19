package com.osleigh.url_shortener.util;

public abstract class Base62 {

  private static final int BASE = 62;
  private static final String[] BASE62_CHARACTERS = new String[] {
    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
    "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
    "U", "V", "W", "X", "Y", "Z",
    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
    "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
    "u", "v", "w", "x", "y", "z"
  };

  public static String encode(long value) {
    StringBuilder sb = new StringBuilder();

    if (value == 0) return "0";

    while (value > 0) {
      int remainder = (int) (value % BASE);
      sb.append(BASE62_CHARACTERS[remainder]);
      value /= BASE;
    }

    return sb.toString();
  }

  public static String getRandomCharacter() {
    int index = (int) (Math.random() * BASE);
    return BASE62_CHARACTERS[index];
  }
}
