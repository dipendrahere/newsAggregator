package code;

import code.clusteringComponent.BatchClusterService;
import code.clusteringComponent.IncrementalClusterService;
import code.contentComponent.PollingService;


public class Main {

    public static void main(String[] args)  {
        BatchClusterService.getInstance().start();
//        IncrementalClusterService.getInstance().start();
        PollingService.getInstance().poll();
    }
}