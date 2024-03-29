package code.clusteringComponent;

import code.models.CategoryType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class BatchClusterService {
    private final static ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
    private List<BatchClusterController> controllers;
    private static BatchClusterService batchClusterService;
    public static BatchClusterService getInstance(){
        if(batchClusterService == null){
            return new BatchClusterService();
        }
        return batchClusterService;
    }

    private BatchClusterService(){

        controllers = Arrays.stream(CategoryType.values()).map(obj -> {
            if(obj == CategoryType.SCITECH)
                return new BatchClusterController(obj, 0.6, 3);
            else
                return new BatchClusterController(obj, 0.5, 4);
        }).collect(Collectors.toList());
    }

    public void start(){
        List<Runnable> runnables = controllers.stream().map(controller -> {
            Runnable r = () ->  controller.startClustering();
            return r;
        }).collect(Collectors.toList());
        for(Runnable runnable: runnables){
            service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.DAYS);
        }
    }
}
