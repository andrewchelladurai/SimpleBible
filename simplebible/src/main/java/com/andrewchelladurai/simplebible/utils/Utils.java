package com.andrewchelladurai.simplebible.utils;

public class Utils {

  public static final int MAX_BOOKS = 66;

  public static final int MAX_VERSES = 31098;

  private static final Utils THIS_INSTANCE = new Utils();

  private Utils() {
  }

  static Utils getInstance() {
    return THIS_INSTANCE;
  }

}
