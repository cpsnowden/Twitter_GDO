package com.cps15.DataService;

import com.cps15.Database.DatabaseWriter;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public class TwitterRestCollector extends TwitterCollector {


        private static final String CONSUMER_SECRET = "zyc5BC1voOMPmJs3G7o1hHv4jI8ii6WGctf4yzdg43xQzxPH0U";
        private static final String CONSUMER_KEY = "uxMqOuKT66k0ihHJ9aQRoCq2x";
        private static final String ACCESS_TOKEN = "529742116-XJ1FPnd2ANhx8UDsv3espl1OAlufhzQT5G6yQDxA";
        private static final String ACCESS_TOKEN_SECRET = "sHoqlokqwo0OPz2RRBjxBL3E8CKszf9dibi8ao4xLykMS";

        private static final Logger logger = Logger.getLogger(com.cps15.DataService.TwitterRestCollector.class.getName());

        private DatabaseWriter dbw;
        private Twitter twitter;
        private List<String> trackTerms;


        public TwitterRestCollector(String databaseName, String collectionName, List<String> trackTerms, String[] auth){
            super(auth[0], auth[1], auth[2], auth[3], databaseName, collectionName);

            this.trackTerms = trackTerms;
            twitter = new TwitterFactory(getBaseConfigurationBuilder().build()).getInstance();
        }


}
