package code.clusteringComponent;

import code.models.CategoryType;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IncrementalService {
    private final static ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
    private List<IncrementalController> controllers;
    private static IncrementalService incrementalService;
    public static IncrementalService getInstance(){
        if(incrementalService == null){
            return new IncrementalService();
        }
        return incrementalService;
    }

    private IncrementalService(){
        controllers = Arrays.stream(CategoryType.values()).map(obj -> {
            return new IncrementalController(obj);
        }).collect(Collectors.toList());
    }

    public void start(){
        List<Runnable> runnables = controllers.stream().map(controller -> {
            Runnable r = () ->  controller.run();
            return r;
        }).collect(Collectors.toList());
        for(Runnable runnable: runnables){
            service.scheduleAtFixedRate(runnable, 0, 5, TimeUnit.MINUTES);
        }
    }

    public void shutdown(){
        service.shutdown();
    }
}
