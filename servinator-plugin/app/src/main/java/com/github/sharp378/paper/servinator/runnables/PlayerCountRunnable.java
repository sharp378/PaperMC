package com.github.sharp378.paper.servinator.runnables;

import com.github.sharp378.paper.servinator.constants.Env;
import com.github.sharp378.paper.servinator.helpers.EcsClientHelper;
import org.bukkit.Bukkit;
import software.amazon.awssdk.services.ecs.model.EcsException;
import software.amazon.awssdk.services.ecs.model.UpdateServiceRequest;

// TODO - logging
public class PlayerCountRunnable implements Runnable {

    private final static boolean isPlayerOnline() {
        return (Bukkit.getOnlinePlayers()).size() != 0;
    }

    @Override
    public void run() {
	if (isPlayerOnline()) {
	    return;
	}

	if (Env.ECS_ENABLED) {
	    try {
		UpdateServiceRequest.Builder request = UpdateServiceRequest.builder()
		    .desiredCount(0);
                EcsClientHelper.updateService(request);
            } catch (EcsException e) {
            }
	}

	Bukkit.shutdown();
    }
}

