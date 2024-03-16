package com.github.sharp378.paper.servinator.runnables;

import com.github.sharp378.paper.servinator.constants.Env;
import com.github.sharp378.paper.servinator.helpers.EcsClientHelper;
import org.bukkit.Bukkit;
import software.amazon.awssdk.services.ecs.model.EcsException;
import software.amazon.awssdk.services.ecs.model.UpdateServiceRequest;

import java.util.function.Consumer;
import java.util.logging.Logger;

public class PlayerCountRunnable implements Runnable {

    private Logger logger;

    private final static boolean isPlayerOnline() {
        return (Bukkit.getOnlinePlayers()).size() != 0;
    }

    public PlayerCountRunnable(Logger logger) {
	super();
        logger = logger;
    } 

    @Override
    public void run() {
	if (isPlayerOnline()) {
	    logger.info("At least one player is connected, skipping shutdown");
	    return;
	}

	if (Env.ECS_ENABLED) {
	    try {
                Consumer<UpdateServiceRequest.Builder> consumer = updateRequest ->
		{
		    updateRequest.desiredCount(0);
		};

                EcsClientHelper.updateSpecificService(consumer);
            } catch (EcsException e) {
		logger.severe("Unable to update ECS service. It will need to be cleaned "
		    + "up a different way: " 
		    + e.awsErrorDetails().errorMessage());
            }
	}

	logger.info("No active players, shutting down server");
	Bukkit.shutdown();
    }
}

