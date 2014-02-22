package com.gospel.ingest;

import java.io.IOException;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.gospel.ingest.save.LucenePlaceDataStorer;
import com.gospel.ingest.save.PlaceDataStorer;

public class ZipLocationModule extends AbstractModule{

  private static final String PROPS_FILE = "zip.properties";

  @Override
  protected void configure() {
    try {
      Properties properties = new Properties();
      properties.load(this.getClass().getClassLoader().getResourceAsStream(PROPS_FILE));
      Names.bindProperties(binder(), properties);
    } catch (IOException e) {
      throw new RuntimeException("Failed to find properties file[" + PROPS_FILE + "] on classpath.");
    }
    
    Multibinder<PlaceDataStorer> storeSet = Multibinder.newSetBinder(binder(), PlaceDataStorer.class);
    storeSet.addBinding().to(LucenePlaceDataStorer.class);
  }

}
