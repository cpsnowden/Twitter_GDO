package com.cps15;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        GraphManager gm = new GraphManager("Twitter");
//        System.out.println(gm.getRetweetGraph("InOrOut_Stream", LayoutEngine.LayoutAlgorithm.YIFANHU, 60));
        System.out.println(gm.getRetweetGraph("Brexit_Stream", LayoutEngine.LayoutAlgorithm.YIFANHU, 180));
        gm.shutdown();

    }
}
