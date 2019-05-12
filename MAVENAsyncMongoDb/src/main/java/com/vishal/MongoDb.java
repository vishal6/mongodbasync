package com.vishal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bson.Document;
import org.eclipse.jetty.util.ajax.JSON;

import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;

import static com.mongodb.client.model.Filters.eq;
/**
 * Java + MongoDB Hello world Example
 * 
 */
public class MongoDb {
	static String result="";
  @SuppressWarnings("deprecation")
public static String getCollectionsFromDb(String param) throws InterruptedException {

    try {

	/**** Connect to MongoDB ****/
	// Since 2.10.0, uses MongoClient
    	/*
    	MongoClientURI uri = new MongoClientURI(
    		    "mongodb+srv://vishal05:H9id11u4JvJTfIg0@cluster0-t92j3.mongodb.net/test?retryWrites=true");*/

    		//MongoClient mongoClient = new MongoClient("localhost",27017);
    	MongoClient mongoClient = MongoClients.create();
    		
    		/*MongoCredential credential;
    	      credential = MongoCredential.createScramSha1Credential("vishal05", "testdb", 
    	         "H9id11u4JvJTfIg0".toCharArray()); 
    	      System.out.println("Connected to the database successfully"); */
    		
    		MongoDatabase database = mongoClient.getDatabase("testdb");
    		
    		final CountDownLatch latch = new CountDownLatch(1);
    		
    		
    		MongoCollection<Document> tables= database.getCollection("sample_json");
    		
    		System.out.println("tables"+tables);
    		//Semaphore semaphore = new Semaphore(0);
    		
    		//semaphore.acquire();

    		Block<Document> printBlock = new Block<Document>() {
    		    public void apply(final Document document) {
    		    	result = document.toJson();
    		        System.out.println("document"+document.toJson());
    		    }
    		};
    		
    		SingleResultCallback<Void> callbackWhenFinished = new SingleResultCallback<Void>() {
    		    public void onResult(final Void result, final Throwable t) {
    		    	latch.countDown();
    		        System.out.println("Operation Finished!");
    		  //      semaphore.release();
    		    }
    		};
    		
    		System.out.println("param"+param);
    		
    		tables.find(eq("_id",param)).forEach(printBlock, callbackWhenFinished);
    		
    		  try {
    	            latch.await();
    	        } catch (InterruptedException e) {
    	            e.printStackTrace();
    	        }
    		

			
    		

	

	/**** Done ****/
	System.out.println("Done");
	mongoClient.close();
	
    } catch (MongoException e) {
	e.printStackTrace();
    } finally {
	}
    return result;

  }
  
  public static void insertIntoCollection(String json){

	  MongoClient mongoClient = MongoClients.create();
		MongoDatabase database = mongoClient.getDatabase("testdb");
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		
		MongoCollection<Document> tables= database.getCollection("sample_json");
		
		Object parse = JSON.parse(json);
		
		System.out.println("parse"+parse);
		
		
		
		/*Document dbObject = (Document)JSON.parse(json);*/
		
		

		tables.insertOne(Document.parse(json), new SingleResultCallback<Void>() {
		    public void onResult(final Void result, final Throwable t) {
		        latch.countDown();
		    	System.out.println("Inserted!");
		    }
		});
		
		 try {
	            latch.await();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		
  }
}