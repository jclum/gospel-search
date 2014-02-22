package com.gospel.ingest.misc;

public class QueryLimitReachedException extends Exception {

  private static final long serialVersionUID = 5652746643553309628L;

  public QueryLimitReachedException(String message) {
    super(message);
  }

}
