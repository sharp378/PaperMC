package com.github.sharp378.paper.servinator;

import com.github.sharp378.paper.servinator.constants.Env;
import com.github.sharp378.paper.servinator.helpers.EcsClientHelper;
import com.github.sharp378.paper.servinator.runnables.PlayerCountRunnable;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import software.amazon.awssdk.core.exception.SdkClientException;

import java.util.concurrent.*;
import java.util.logging.Logger;

public class ServinatorPlugin extends JavaPlugin {
    
    private final static ThreadFactory playerCountThreadFactory = new ThreadFactoryBuilder()
        .setNameFormat("PlayerCount")
        .build();
    
    private ScheduledExecutorService playerCountExecutor;

    private static String createAndVerifyEcsClient() {
        try {
	    EcsClientHelper.initClient();
	} catch (SdkClientException e) {
            return "There was a problem setting up the ECS client: "
		+ e.getMessage();
	}

	return "";
    }

    @Override
    public void onEnable() {
	final Logger logger = getLogger();
	
	if (Env.ECS_ENABLED) {
	    final String error = createAndVerifyEcsClient();
	    if (error.length() > 0) {
	        logger.severe(error);
		this.setEnabled(false);
		return;
	    }

	    logger.info("ECS service handling is enabled");
	}

	final long delay = Env.PLUGIN_DELAY; 
	logger.info("Checking for server inactivity with delay: " + delay + " minutes");
	playerCountExecutor = Executors.newSingleThreadScheduledExecutor(playerCountThreadFactory);
        playerCountExecutor.scheduleWithFixedDelay(new PlayerCountRunnable(logger), delay, delay, TimeUnit.MINUTES);
    }

    @Override
    public void onDisable() {
	if (playerCountExecutor != null) {
	    playerCountExecutor.shutdown();
	}

        EcsClientHelper.closeClient();
    }
}

