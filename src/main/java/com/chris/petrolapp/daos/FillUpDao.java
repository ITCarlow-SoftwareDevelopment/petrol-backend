package com.chris.petrolapp.daos;

import com.chris.petrolapp.objects.FillUp;
import com.chris.petrolapp.objects.Result;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static java.lang.Math.toIntExact;

public class FillUpDao {
  private DatastoreService datastore;
  private static final String FILLUP_KIND = "FillUp1";

  public FillUpDao() {
    datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
  }

  public FillUp entityToFillUp(Entity entity) {
    FillUp fillUp = new FillUp();    
    fillUp.setId(entity.getKey().getId());
    fillUp.setDateTime((long)entity.getProperty(FillUp.DATETIME));
    fillUp.setOdometer(toIntExact((long)entity.getProperty(FillUp.ODOMETER))); //toIntExact new in Java 8: may raise ArithmeticException 
    fillUp.setPrice(toIntExact((long)entity.getProperty(FillUp.PRICE)));
    fillUp.setPartial((boolean)entity.getProperty(FillUp.PARTIAL));
    return fillUp;
  }
 
  public Long createFillUp(FillUp fillUp) {
    Entity incFillUpEntity = new Entity(FILLUP_KIND);  // Key will be assigned once written
    incFillUpEntity.setProperty(FillUp.DATETIME, fillUp.getDateTime());
    incFillUpEntity.setProperty(FillUp.ODOMETER, fillUp.getOdometer());
    incFillUpEntity.setProperty(FillUp.PRICE, fillUp.getPrice());
    incFillUpEntity.setProperty(FillUp.VOLUME, fillUp.getVolume());
    incFillUpEntity.setProperty(FillUp.PARTIAL, fillUp.getPartial());

    Key fillUpKey = datastore.put(incFillUpEntity); // Save the Entity
    return fillUpKey.getId();                     // The ID of the Key
  }

  
  public List<FillUp> entitiesToFillUps(Iterator<Entity> results) {
    List<FillUp> resultFillUps = new ArrayList<>();
    while (results.hasNext()) {  // We still have data
      resultFillUps.add(entityToFillUp(results.next()));      // Add the FillUp to the List
    }
    return resultFillUps;
  }

  public Result<FillUp> listFillUps(String startCursorString) {
    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(10); // Only show 10 at a time
    if (startCursorString != null && !startCursorString.equals("")) { // a cursor was passed
      fetchOptions.startCursor(Cursor.fromWebSafeString(startCursorString)); // Where we left off
    }
    Query query = new Query(FILLUP_KIND) // We only care about Fill Ups
        .addSort(FillUp.DATETIME, SortDirection.DESCENDING); // most recents first
    PreparedQuery preparedQuery = datastore.prepare(query);
    QueryResultIterator<Entity> results = preparedQuery.asQueryResultIterator(fetchOptions);

    List<FillUp> resultFillUps = entitiesToFillUps(results);     // Retrieve and convert to fillUps
    Cursor cursor = results.getCursor();              // Where to start next time
    if (cursor != null && resultFillUps.size() == 10) {         // Are we paging? Save Cursor
      String cursorString = cursor.toWebSafeString();               // Cursors are WebSafe
      return new Result<>(resultFillUps, cursorString);
    } else {
      return new Result<>(resultFillUps);
    }
  }

}
