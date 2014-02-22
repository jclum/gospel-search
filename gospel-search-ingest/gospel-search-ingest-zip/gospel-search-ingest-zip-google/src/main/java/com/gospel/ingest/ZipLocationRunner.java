package com.gospel.ingest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.gospel.ingest.misc.UnableToQueryGooglePlaces;

public class ZipLocationRunner {
  public static void main(String[] args) throws UnableToQueryGooglePlaces{
    Injector injector = Guice.createInjector(new ZipLocationModule());
    RedisZipPlacesFinder placesFinder = injector.getInstance(RedisZipPlacesFinder.class);
    System.exit(placesFinder.processZipCodes());
  }
}
