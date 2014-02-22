package com.gospel.ingest.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Places;
import net.sf.sprockets.google.Places.Field;
import net.sf.sprockets.google.Places.Params;
import net.sf.sprockets.google.Places.Response;
import net.sf.sprockets.google.Places.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.gospel.ingest.IngestConfiguration;
import com.gospel.ingest.misc.QueryLimitReachedException;
import com.gospel.ingest.misc.UnableToQueryGooglePlaces;

public class GeoLocationQuerier {
  
  private static final Logger LOG = LogManager.getLogger(GeoLocationQuerier.class);
  
  private final DetailedPlaceFinder detailedPlaceFinder;
  private final String searchTerm;
  private final int radiusInMeters;
  private final int maxResults;

  @Inject
  public GeoLocationQuerier(DetailedPlaceFinder detailedPlaceFinder,
      @Named(IngestConfiguration.SearchTerm.NAME) String searchTerm, 
      @Named(IngestConfiguration.MaxSearchRadius.NAME) int radiusInMeters, 
      @Named(IngestConfiguration.MaxSearchResults.NAME) int maxResults){
    this.detailedPlaceFinder = detailedPlaceFinder;
    this.searchTerm = searchTerm;
    this.radiusInMeters = radiusInMeters;
    this.maxResults = maxResults;
  }
  
  public List<Place> findPlaceDetails(Double latitude, 
      Double longitude) throws UnableToQueryGooglePlaces, QueryLimitReachedException {
    
    List<Place> detailedPlaces = new ArrayList<Place>();
    
    Response<List<Place>> query;
    try {
      
      query = Places.nearbySearch(
          new Params().location(latitude, longitude)
            .radius(radiusInMeters)
            .keyword(searchTerm)
            .maxResults(maxResults), 
          Field.NAME);
      
    } catch (IOException e) {
      LOG.error("Could not query for place location at zip[], long[], lat[]", e);
      throw new UnableToQueryGooglePlaces("Could not query for place location", e);
    }
    
    if(LOG.isDebugEnabled()){
      LOG.debug("Status returned: " + query.getStatus());
    }
    
    if(Status.OVER_QUERY_LIMIT.toString().equals(query.getStatus().toString())){
      throw new QueryLimitReachedException("Query limit reached.");
    }
    
    List<Place> places = query.getResult();

    if(places != null && places.size() != 0){
      for(Place place : places){
        Place detailedPlace = detailedPlaceFinder.findDetailedPlace(place);
        if(detailedPlace != null){
          detailedPlaces.add(detailedPlace);
        }
      }
    } else {
      if(LOG.isDebugEnabled()){
        LOG.debug("No places found.");
      }
    }
    
    return detailedPlaces;
  }
}
