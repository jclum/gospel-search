package com.gospel.ingest.save;

import net.sf.sprockets.google.Place;

public interface PlaceDataStorer {
  public void save(Place place);
  public void close();
}
