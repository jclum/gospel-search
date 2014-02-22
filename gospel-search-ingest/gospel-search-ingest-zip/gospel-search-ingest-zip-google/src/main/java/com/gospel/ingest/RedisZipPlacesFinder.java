package com.gospel.ingest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.sprockets.Sprockets;
import net.sf.sprockets.google.Place;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.gospel.ingest.IngestConfiguration.CompletionStatus;
import com.gospel.ingest.IngestConfiguration.IngestStatus;
import com.gospel.ingest.misc.QueryLimitReachedException;
import com.gospel.ingest.misc.UnableToQueryGooglePlaces;
import com.gospel.ingest.query.GeoLocationQuerier;
import com.gospel.ingest.save.PlaceDataSaver;

public class RedisZipPlacesFinder {
  
  private static final Logger LOG = LogManager.getLogger(RedisZipPlacesFinder.class);
  
  private final GeoLocationQuerier locationQuerier;
  private final String keyPrefix;
  private final String statusKey;
  private final String redisLocation;
  private final String apiKey;
  private final String zipLocation;
  private final PlaceDataSaver placeStore;
  
  @Inject
  public RedisZipPlacesFinder(
      GeoLocationQuerier locationQuerier,
      PlaceDataSaver placeStore,
      @Named(IngestConfiguration.ApiKey.NAME) String apiKey,
      @Named(IngestConfiguration.RedisLocation.NAME) String redisLocation,
      @Named(IngestConfiguration.IngestStatusKey.NAME) String statusKey,
      @Named(IngestConfiguration.KeyPrefix.NAME) String keyPrefix,
      @Named(IngestConfiguration.ZipLocation.NAME) String zipLocation){
    this.locationQuerier = locationQuerier;
    this.placeStore = placeStore;
    this.apiKey = apiKey;
    this.redisLocation = redisLocation;
    this.statusKey = statusKey;
    this.keyPrefix = keyPrefix;
    this.zipLocation = zipLocation;
  }

  public int processZipCodes() throws UnableToQueryGooglePlaces {
    
    Sprockets.getConfig().setProperty("google.api-key", apiKey);
    
    // Set up Jedis
    Jedis jedis = new Jedis(redisLocation);
    
    String status = jedis.get(statusKey);
    // If no status or status is done, load REDIS with zip codes and start over
    if(status == null || CompletionStatus.NAME.equals(status)){
      
      if(status == null) jedis.set(statusKey, IngestStatus.NAME);
      
      Set<String> keys = jedis.keys(keyPrefix + ":*");
      Iterator<String> iterator = keys.iterator();
      while(iterator.hasNext()) {
        jedis.del(iterator.next());
      }
      try{
        BufferedReader br = new BufferedReader(new FileReader(zipLocation));
        String line;
        while ((line = br.readLine()) != null) {
          int firstComma = line.indexOf(",");
          if(firstComma != -1){
            String zip = line.substring(0, firstComma);
            String coordinates = line.substring(firstComma + 1);
            jedis.set(keyPrefix + ":" + zip, coordinates);
          } else {
            LOG.error("Invalid line[" + line + "]");
          }
        }
        br.close();
      } catch (IOException e) {
        throw new RuntimeException();
      }
    }
    
    // get available keys and start ingesting data
    Set<String> keys = jedis.keys(keyPrefix + ":*");
    
    if(LOG.isInfoEnabled()){
      LOG.info("Processing " + keys.size() + " zipcodes..");
    }
    
    Iterator<String> iterator = keys.iterator();
    while(iterator.hasNext()) {
      String key = iterator.next();
      String value = jedis.get(key);
      if(value != null){
        String[] values = value.split(",");
        if(values.length == 2) {
          Double lat = Double.parseDouble(values[0]);
          Double lon = Double.parseDouble(values[1]);
          List<Place> places;
          try {
            places = locationQuerier.findPlaceDetails(lat, lon);
          } catch (QueryLimitReachedException e) {
            LOG.error("Query Limit Reached.");
            placeStore.close();
            return 1;
          }
          if(places != null){
            for(Place place : places){
              placeStore.save(place);
            }
          }
          // TODO: Re-add delete
          //jedis.del(key);
        } else {
          LOG.error("Invalid value[" + value + "]");
        }
      }
    }
    
    return 0;
  }
}
