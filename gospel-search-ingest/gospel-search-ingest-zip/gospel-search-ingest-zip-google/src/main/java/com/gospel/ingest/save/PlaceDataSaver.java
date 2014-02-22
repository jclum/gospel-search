package com.gospel.ingest.save;

import java.util.Set;

import net.sf.sprockets.google.Place;

import com.google.inject.Inject;

public class PlaceDataSaver {
  
  private Set<PlaceDataStorer> storeSet;
  
  @Inject
  public PlaceDataSaver(Set<PlaceDataStorer> storeSet){
    this.storeSet = storeSet;
  }
  
  public void save(Place place){
    for(PlaceDataStorer dataStorer : storeSet){
      dataStorer.save(place);
    }
  }
  
  public void close(){
    for(PlaceDataStorer dataStorer : storeSet){
      dataStorer.close();
    }
  }
}
