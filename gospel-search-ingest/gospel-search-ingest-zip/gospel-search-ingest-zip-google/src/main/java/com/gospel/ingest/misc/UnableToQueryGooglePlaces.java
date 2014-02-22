package com.gospel.ingest.misc;


public class UnableToQueryGooglePlaces extends Exception {

  private static final long serialVersionUID = -6436926433879473874L;

  public UnableToQueryGooglePlaces(String message) {
    super(message);
  }
  
  public UnableToQueryGooglePlaces(Throwable e) {
    super(e);
  }
  
  public UnableToQueryGooglePlaces(String message, Throwable e) {
    super(message, e);
  }
  

}
