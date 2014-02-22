package com.gospel.ingest.misc;


public class UnableToWriteResultsException extends Exception {

  private static final long serialVersionUID = 7181827787036608598L;

  public UnableToWriteResultsException(String message, Throwable e) {
    super(message, e);
  }

}
