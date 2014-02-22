package com.gospel.ingest.save;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Place.Review;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.gospel.ingest.IngestConfiguration.LuceneIndexLocation;
import com.gospel.ingest.misc.UnableToWriteResultsException;

public class LucenePlaceDataStorer implements PlaceDataStorer {
  
  public static final Logger LOG = LogManager.getLogger(LucenePlaceDataStorer.class);
  
  public static final String LUCENE_DELIM = " ";
  
  static final Function<Review, String> REVIEW_FUNC = new Function<Review, String>() {
    public String apply(Review r) {
      return r.getText() + LUCENE_DELIM + r.getAuthorName() + LUCENE_DELIM + r.getLanguage() + LUCENE_DELIM + r.getTime();
    }
  };
  
  private IndexWriter writer;
  private Directory indexDir;

  @Inject
  public LucenePlaceDataStorer(@Named(LuceneIndexLocation.NAME) String lucenIndexLocation) throws UnableToWriteResultsException{
    try {
      indexDir = FSDirectory.open(new File(lucenIndexLocation));
  
      Version luceneVersion = Version.LUCENE_46;
      IndexWriterConfig luceneConfig = new IndexWriterConfig(
      luceneVersion, new StandardAnalyzer(luceneVersion));

      writer = new IndexWriter(indexDir, luceneConfig);
    } catch (IOException e) {
      throw new UnableToWriteResultsException("Could not initialize the writer.", e);
    }
  }

  public void save(Place place) {
    try {
      Document doc = new Document();

      if(place.getAddress() != null) {
        String adminAreaL1 = place.getAddress().getAdminAreaL1();
        if (adminAreaL1 != null) {
          doc.add(new TextField("adminAreaL1", adminAreaL1, Store.YES)); 
        }
  
        String adminAreaL1Abbr = place.getAddress().getAdminAreaL1Abbr();
        if (adminAreaL1Abbr != null) {
          doc.add(new TextField("adminAreaL1Abbr", adminAreaL1Abbr, Store.YES));
        }
        
        String adminAreaL2 = place.getAddress().getAdminAreaL2();
        if (adminAreaL2 != null) {
          doc.add(new TextField("adminAreaL2", adminAreaL2, Store.YES));
        }
        
        String adminAreaL2Abbr = place.getAddress().getAdminAreaL2Abbr();
        if (adminAreaL2Abbr != null) {
          doc.add(new TextField("adminAreaL2Abbr", adminAreaL2Abbr, Store.YES));
        }
        
        String country = place.getAddress().getCountry();
        if (country != null) {
          doc.add(new TextField("country", country, Store.YES));
        }
        
        String countryAbbr = place.getAddress().getCountryAbbr();
        if (countryAbbr != null) {
          doc.add(new TextField("countryAbbr", countryAbbr, Store.YES));
        }
        
        String locality = place.getAddress().getLocality();
        if (locality != null) {
          doc.add(new TextField("locality", locality, Store.YES));
        }
        
        String localityAbbr = place.getAddress().getLocalityAbbr();
        if (localityAbbr != null) {
          doc.add(new TextField("localityAbbr", localityAbbr, Store.YES));
        }
        
        String postalCode = place.getAddress().getPostalCode(); 
        if (postalCode != null) {
          doc.add(new TextField("postalCode", postalCode, Store.YES));
        }
        
        String postalCodeAbbr = place.getAddress().getPostalCodeAbbr();
        if (postalCodeAbbr != null) {
          doc.add(new TextField("postalCodeAbbr", postalCodeAbbr, Store.YES));
        }
        
        String postalTown = place.getAddress().getPostalTown();
        if (postalTown != null) {
          doc.add(new TextField("postalTown", postalTown, Store.YES));
        }
        
        String postalTownAbbr = place.getAddress().getPostalTownAbbr();
        if (postalTownAbbr != null) {
          doc.add(new TextField("postalTownAbbr", postalTownAbbr, Store.YES));
        }
        
        String postalTownRoute = place.getAddress().getPostalTown();
        if (postalTownRoute != null) {
          doc.add(new TextField("postalTownRoute", postalTownRoute, Store.YES));
        }
        
        String route = place.getAddress().getRoute();
        if (route != null) {
          doc.add(new TextField("route", route, Store.YES));
        }
        
        String routeAbbr = place.getAddress().getRouteAbbr();
        if(routeAbbr != null){
          doc.add(new TextField("routeAbbr", routeAbbr, Store.YES));
        }
        
        String streetAddress = place.getAddress().getStreetNumber();
        if (streetAddress != null) {
          doc.add(new TextField("streetAddress", streetAddress, Store.YES));
        }
        
        String streadNumAbbr = place.getAddress().getStreetNumberAbbr();
        if (streadNumAbbr != null) {
          doc.add(new TextField("streadNumAbbr", streadNumAbbr, Store.YES));
        }
        
        String sublocality = place.getAddress().getSublocality();
        if (sublocality != null) {
          doc.add(new TextField("sublocality", sublocality, Store.YES));
        }
        
        String sublocalityAbbr = place.getAddress().getSublocalityAbbr();
        if (sublocalityAbbr != null) {
          doc.add(new TextField("sublocalityAbbr", sublocalityAbbr, Store.YES));
        }
      }
      
      String address = place.getFormattedAddress();
      if (address != null) {
        doc.add(new TextField("address", address, Store.YES));
      }
      
      String phone = place.getFormattedPhoneNumber();
      if (phone != null) {
        doc.add(new TextField("phone", phone, Store.YES));
      }
      
      String icon = place.getIcon();
      if (icon != null) {
        doc.add(new TextField("icon", icon, Store.YES));
      }
      
      String id = place.getId();
      if (id != null) {
        doc.add(new TextField("id", id, Store.YES));
      }
      
      String internationPhone = place.getIntlPhoneNumber();
      if (internationPhone != null) {
        doc.add(new TextField("internationPhone", internationPhone, Store.YES));
      }
      
      double latitude = place.getLatitude();
      doc.add(new TextField("latitude", String.valueOf(latitude), Store.YES));
      
      double longitude = place.getLongitude();
      doc.add(new TextField("longitude", String.valueOf(longitude), Store.YES));
      
      String name = place.getName();
      if (name != null) {
        doc.add(new TextField("name", name, Store.YES));
      }
      
      float rating = place.getRating();
      doc.add(new FloatField("rating", rating, Store.YES));
      
      String ref = place.getReference();
      if (ref != null) {
        doc.add(new TextField("reference", ref, Store.YES));
      }
      
      String googlePage = place.getUrl();
      if (googlePage != null) {
        doc.add(new TextField("googlePage", googlePage, Store.YES));
      }
      
      String vicinity = place.getVicinity();
      if(vicinity != null){
        doc.add(new TextField("vicinity", vicinity, Store.YES));
      }
      
      String website = place.getWebsite();
      if(website != null){
        doc.add(new TextField("website", website, Store.YES));
      }
      
      List<String> typesList = place.getTypes();
      if(typesList != null) {
        String types = Arrays.toString(typesList.toArray(new String[typesList.size()]));
        doc.add(new TextField("types", types, Store.YES));
      }
      
      List<Review> reviewsList = place.getReviews();
      if(reviewsList != null){
        List<String> stringReviewsList = Lists.newArrayList(Collections2.transform(place.getReviews(), REVIEW_FUNC));
        if(stringReviewsList != null){
          String reviews = Arrays.toString(stringReviewsList.toArray(new String[stringReviewsList.size()])); 
          doc.add(new TextField("reviews", reviews, Store.YES));
        }
      }

      writer.addDocument(doc);
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }

  public void close(){
    try {
      writer.close();
      indexDir.close();
    } catch (IOException e) {
      LOG.error("Could not close lucene directory properly");
    }
  }

}
