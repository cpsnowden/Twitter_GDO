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
        GraphManager graphManager = new GraphManager("Twitter");
        System.out.println(graphManager.getRetweetGraph("Brexit_Stream"));
    }

}
