package code.contentComponent;

import code.databaseService.DBConnect;
import code.models.CategoryType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PollingService {
    private final static ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

    private List<RssController> controllers;
    private static PollingService pollingService;

    public static PollingService getInstance(){
        if (pollingService==null)
            pollingService = new PollingService();
        return pollingService;
    }

    private PollingService(){
        controllers = Arrays.stream(CategoryType.values()).map(obj -> {
            return new RssController(obj);
        }).collect(Collectors.toList());
    }

    public void poll(){
        List<Runnable> runnables = controllers.stream().map(controller -> {
            Runnable r = () ->  controller.visitCategory();
            return r;
        }).collect(Collectors.toList());
        for(Runnable runnable: runnables){
            service.scheduleAtFixedRate(runnable, 0, 2, TimeUnit.HOURS);
        }
    }
}
