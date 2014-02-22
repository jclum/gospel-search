package com.gospel.ingest;

public class IngestConfiguration {
  
  /**
   * See {@value ApiKey#DESCRIPTION}
   */
  public static final class ApiKey {
    public static final String NAME = "google.places.api.key";
    public static final String DESCRIPTION = "API key for Google Places API";
    public static final String DEFAULT = null;
  }
  
  /**
   * See {@value RedisLocation#DESCRIPTION}
   */
  public static final class RedisLocation {
    public static final String NAME = "google.places.redis.location";
    public static final String DESCRIPTION = "Location hosting redis server";
    public static final String DEFAULT = "localhost";
  }
  
  /**
   * See {@value SearchTerm#DESCRIPTION}
   */
  public static final class SearchTerm {
    public static final String NAME = "google.places.search.term";
    public static final String DESCRIPTION = "Target search term for Google Places API";
    public static final String DEFAULT = null;
  }
  
  /**
   * See {@value MaxSearchResults#DESCRIPTION}
   */
  public static final class MaxSearchResults {
    public static final String NAME = "google.places.search.max.results";
    public static final String DESCRIPTION = "Max Number of results to pull back from google Places API";
    public static final String DEFAULT = "100";
  }
  
  /**
   * See {@value MaxSearchRadius#DESCRIPTION}
   */
  public static final class MaxSearchRadius {
    public static final String NAME = "google.places.search.radius";
    public static final String DESCRIPTION = "";
    public static final String DEFAULT = "5000 ";
  }
  
  /**
   * See {@value IngestStatus#DESCRIPTION}
   */
  public static final class IngestStatus {
    public static final String NAME = "google.places.ingest.status";
    public static final String DESCRIPTION = "Ingest status used while ingesting data";
    public static final String DEFAULT = "INGESTING";
  }
  
  /**
   * See {@value CompletionStatus#DESCRIPTION}
   */
  public static final class CompletionStatus {
    public static final String NAME = "google.places.completion.status";
    public static final String DESCRIPTION = "Ingest status used when ingest is complete.";
    public static final String DEFAULT = "COMPLETE";
  }
  
  /**
   * See {@value IngestTimeKey#DESCRIPTION}
   */
  public static final class IngestTimeKey{
    public static final String NAME = "google.places.ingest.time.key";
    public static final String DESCRIPTION = "Time key for ingest.";
    public static final String DEFAULT = "IngestTime";
  }
  
  /**
   * See {@value IngestStatusKey#DESCRIPTION}
   */
  public static final class IngestStatusKey {
    public static final String NAME = "google.places.ingest.status.key";
    public static final String DESCRIPTION = "Status  key for ingest.";
    public static final String DEFAULT = "IngestStatus";
  }
  
  /**
   * See {@value KeyPrefix#DESCRIPTION}
   */
  public static final class KeyPrefix {
    public static final String NAME = "google.places.key.prefix";
    public static final String DESCRIPTION = "Prefix for keys to ingest.";
    public static final String DEFAULT = "KEY:";
  }
  
  /**
   * See {@value ZipLocation#DESCRIPTION}
   */
  public static final class ZipLocation {
    public static final String NAME = "google.places.zip.location";
    public static final String DESCRIPTION = "Location of zip codes to search over. CSV -> ZIP,LAT,LONG";
    public static final String DEFAULT = "KEY:";
  }
  
  /**
   * See {@value LuceneIndexLocation#DESCRIPTION}
   */
  public static final class LuceneIndexLocation {
    public static final String NAME = "google.places.index.location.lucene";
    public static final String DESCRIPTION = "Location of lucene index on disk.";
    public static final String DEFAULT = null;
  }
  
}
