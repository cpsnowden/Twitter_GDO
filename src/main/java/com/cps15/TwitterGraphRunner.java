package com.cps15;

import com.cps15.AnalyticsService.Graph.GraphManager;

/**
 * Twitter_GDO
 * Created by chris on 10/06/2016.
 */
public class TwitterGraphRunner
{
    public static void main( String[] args )
    {
        System.out.println(Thread.activeCount());
        GraphManager graphManager = new GraphManager("Twitter", false);
        System.out.println(graphManager.getRetweetGraph("Trump_Clinton",10,2));
        graphManager.close();
        System.out.println("End");
        System.out.println(Thread.activeCount());
        System.exit(0);

    }

}
