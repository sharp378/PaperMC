package com.github.sharp378.paper.servinator.runnables;

import com.github.sharp378.paper.servinator.helpers.EcsClientHelper;
import org.bukkit.Bukkit;
import software.amazon.awssdk.services.ecs.model.EcsException;
import software.amazon.awssdk.services.ecs.model.UpdateServiceRequest;

import java.util.logging.Logger;

public class PlayerCountRunnable implements Runnable {

    private Logger logger;
    private boolean ecsEnabled;

    private final static boolean isPlayerOnline() {
        return (Bukkit.getOnlinePlayers()).size() != 0;
    }

    public PlayerCountRunnable(Logger logger, boolean ecsEnabled) {
	super();
        logger = logger;
	ecsEnabled = ecsEnabled;
    } 

    @Override
    public void run() {
	if (isPlayerOnline()) {
	    logger.info("At least one player is connected, skipping shutdown");
	    return;
	}

	if (ecsEnabled) {
	    try {
                UpdateServiceRequest serviceRequest = UpdateServiceRequest.builder()
                    //.cluster()
                    //.service()
                    .desiredCount(0)
                    .build();

                EcsClientHelper.updateSpecificService(serviceRequest);

            } catch (EcsException e) {
                //System.err.println(e.awsErrorDetails().errorMessage());
		logger.severe("Unable to update ECS service. It will need to be cleaned up by a different means");
            }
	}

	Bukkit.shutdown();
    }
}

