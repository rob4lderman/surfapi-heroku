package com.surfapi.javadoc;

import org.apache.commons.lang3.StringUtils;

import com.surfapi.db.MongoDBService;
import com.surfapi.log.Log;

/**
 * Sets Mongo as the DBService provider, then calls ServerMain to start the server.
 * 
 * Note: -Dcom.surfapi.mongo.db.name must be set.
 * 
 */
public class ResetMongoMain {

    /**
     * Wrap ServerMain().  Sets mongo as the db provider.
     * 
     */
    public static void main(String[] args) throws Exception {
        
        verifyEnv();
        
        if (args.length == 0) {
            usage();
        } else if (args[0].equals("--db")) {
            MongoDBService.getDb().drop();
            Log.info( "ResetMongoMain: main: Dropped DB: " + MongoDBService.getDbName());
        } else {
            MongoDBService.getDb().drop( args[0] );
            Log.info( "ResetMongoMain: main: Dropped collection: " + MongoDBService.getDbName() + "." + args[0]);
        }
    }
    
    /**
     * 
     */
    protected static void usage() {
        Log.info("ResetMongoMain: usage: <collection-name> | --db");
        System.exit(-1);
    }
    
    /**
     * Verify the mongo db name has been configured in the env.
     */
    protected static void verifyEnv() {
        if (StringUtils.isEmpty( System.getProperty("com.surfapi.mongo.db.name") ) ) {
            throw new RuntimeException("System property 'com.surfapi.mongo.db.name' must be set");
        }
    }
    
}
