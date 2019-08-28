package code.clusteringComponent;

import code.models.CategoryType;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IncrementalClusterService {
    private final static ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
    private List<IncrementalClusterController> controllers;
    private static IncrementalClusterService incrementalClusterService;
    public static IncrementalClusterService getInstance(){
        if(incrementalClusterService == null){
            return new IncrementalClusterService();
        }
        return incrementalClusterService;
    }

    private IncrementalClusterService(){
        controllers = Arrays.stream(CategoryType.values()).map(obj -> {
            return new IncrementalClusterController(obj);
        }).collect(Collectors.toList());
    }

    public void start(){
        List<Runnable> runnables = controllers.stream().map(controller -> {
            Runnable r = () ->  controller.run();
            return r;
        }).collect(Collectors.toList());
        for(Runnable runnable: runnables){
            service.scheduleAtFixedRate(runnable, 0, 6, TimeUnit.MINUTES);
        }
    }

    public void shutdown(){
        service.shutdown();
    }

    public void shutdownNow() throws Exception{
        service.shutdownNow();
    }
}
