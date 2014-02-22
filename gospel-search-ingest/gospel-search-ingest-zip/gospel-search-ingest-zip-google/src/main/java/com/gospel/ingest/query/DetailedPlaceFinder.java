package com.gospel.ingest.query;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Places;
import net.sf.sprockets.google.Places.Field;
import net.sf.sprockets.google.Places.Params;
import net.sf.sprockets.google.Places.Response;
import net.sf.sprockets.google.Places.Response.Status;

import com.gospel.ingest.misc.QueryLimitReachedException;
import com.gospel.ingest.misc.UnableToQueryGooglePlaces;

public class DetailedPlaceFinder {
  
  private static final Logger LOG = LogManager.getLogger(GeoLocationQuerier.class);

  public Place findDetailedPlace(Place place) throws UnableToQueryGooglePlaces, QueryLimitReachedException{
    Place detailedPlace;
    try {
      Response<Place> request = Places.details(new Params().reference(place.getReference()), 
          Field.ADDRESS,
          Field.EVENTS,
          Field.FORMATTED_ADDRESS,
          Field.FORMATTED_PHONE_NUMBER,
          Field.GEOMETRY,
          Field.ICON,
          Field.INTL_PHONE_NUMBER,
          Field.MATCHED_SUBSTRINGS,
          Field.NAME,
          Field.OPEN_NOW,
          Field.PHOTOS,
          Field.PRICE_LEVEL,
          Field.RATING,
          Field.REVIEWS,
          Field.TERMS,
          Field.TYPES,
          Field.URL,
          Field.UTC_OFFSET,
          Field.VICINITY,
          Field.WEBSITE);
          
     detailedPlace = request.getResult();
       
     if(Status.OVER_QUERY_LIMIT.toString().equals(request.getStatus().toString())){
       throw new QueryLimitReachedException("Query limit reached.");
     }
     
      if(detailedPlace != null){
        if(LOG.isDebugEnabled()){
          LOG.debug("Place found: " 
              + "name[" + detailedPlace.getName() + "] "
              + "website[" + detailedPlace.getWebsite() + "] "
              + "latitude[" + detailedPlace.getLatitude() + "] "
              + "longitude[" + detailedPlace.getLongitude() + "]");
        }
      }
    } catch (IOException e) {
      LOG.error("Could not query for " + place.getName() + " details", e);
      throw new UnableToQueryGooglePlaces("Could not query for " + place.getName() + " details", e);
    }
    
    return detailedPlace;
  }
}
