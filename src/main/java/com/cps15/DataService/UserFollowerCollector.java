package com.cps15.DataService;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Map;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public class UserFollowerCollector {

    private static final String CONSUMER_SECRET = "zyc5BC1voOMPmJs3G7o1hHv4jI8ii6WGctf4yzdg43xQzxPH0U";
    private static final String CONSUMER_KEY = "uxMqOuKT66k0ihHJ9aQRoCq2x";
    private static final String ACCESS_TOKEN = "529742116-XJ1FPnd2ANhx8UDsv3espl1OAlufhzQT5G6yQDxA";
    private static final String ACCESS_TOKEN_SECRET = "sHoqlokqwo0OPz2RRBjxBL3E8CKszf9dibi8ao4xLykMS";

    public static void main(String[] args) {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(CONSUMER_KEY);
        cb.setOAuthConsumerSecret(CONSUMER_SECRET);
        cb.setOAuthAccessToken(ACCESS_TOKEN);
        cb.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        cb.setJSONStoreEnabled(true);


        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        Map<String, RateLimitStatus> rateLimitStatus = null;
        RateLimitStatus rls;
        try {
            rateLimitStatus = twitter.getRateLimitStatus("followers");
            System.out.println(rateLimitStatus.keySet());
            rls = rateLimitStatus.get("/followers/ids");

            System.out.printf("You have %d calls remaining out of %d, Limit resets in %d seconds\n",
                    rls.getRemaining(),
                    rls.getLimit(),
                    rls.getSecondsUntilReset());

        } catch (TwitterException e) {
            e.printStackTrace();
            return;
        }

        User user = null;

        try {
            if (rls.getRemaining() == 0)
            {
                //  Yes we do, unfortunately ...
                System.out.printf("!!! Sleeping for %d seconds due to rate limits\n", rls.getSecondsUntilReset());

                //  If you sleep exactly the number of seconds, you can make your query a bit too early
                //  and still get an error for exceeding rate limitations
                //
                //  Adding two seconds seems to do the trick. Sadly, even just adding one second still triggers a
                //  rate limit exception more often than not.  I have no idea why, and I know from a Comp Sci
                //  standpoint this is really bad, but just add in 2 seconds and go about your business.  Or else.
                try {
                    Thread.sleep((rls.getSecondsUntilReset()+2) * 1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            user = twitter.showUser("cpsnowden");
            IDs ids = twitter.getFollowersIDs(user.getId(), -1);
            System.out.println(user.getName());



            System.out.println(rls.getRemaining());

            for(long userID: ids.getIDs()) {
            if (rls.getRemaining() == 0)
            {
                //  Yes we do, unfortunately ...
                System.out.printf("!!! Sleeping for %d seconds due to rate limits\n", rls.getSecondsUntilReset());

                //  If you sleep exactly the number of seconds, you can make your query a bit too early
                //  and still get an error for exceeding rate limitations
                //
                //  Adding two seconds seems to do the trick. Sadly, even just adding one second still triggers a
                //  rate limit exception more often than not.  I have no idea why, and I know from a Comp Sci
                //  standpoint this is really bad, but just add in 2 seconds and go about your business.  Or else.
                try {
                    Thread.sleep((rls.getSecondsUntilReset()+2) * 1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

                System.out.println("Checking " + twitter.showUser(userID).getName());

                IDs followers_followers = twitter.getFollowersIDs(userID,-1);


                for(long ff: followers_followers.getIDs()) {
                    System.out.println(userID + " <- "  + ff);

                }

                System.out.printf("You have %d calls remaining out of %d, Limit resets in %d seconds\n",
                        rls.getRemaining(),
                        rls.getLimit(),
                        rls.getSecondsUntilReset());


            }

        } catch (TwitterException e) {
            e.printStackTrace();
        }


    }



}


