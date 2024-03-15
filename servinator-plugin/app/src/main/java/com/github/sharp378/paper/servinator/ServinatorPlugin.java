package com.github.sharp378.paper.servinator;

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

    private static String verifyEcsClient() {
        try {
	    EcsClientHelper.getClient();
	} catch (SdkClientException e) {
            return "There was a problem setting up the ECS client: "
		+ e.getMessage();
	}

	if (EcsClientHelper.getClientServiceArn() == "") {
	    return "Server is not running in ECS, to use this plugin please "
	        + "either disable ECS in config.yaml or deploy on ECS.";
	}

	return "";
    }

    @Override
    public void onEnable() {
	saveDefaultConfig();
        
	final FileConfiguration config = getConfig();
	final Logger logger = getLogger();

	final long delay = config.getLong("delay", 5); 
	final boolean ecsEnabled = config.getBoolean("ecs.enabled", false);
	
	if (ecsEnabled) {
	    final String error = verifyEcsClient();
	    if (error.length() > 0) {
	        logger.severe(error);
		this.setEnabled(false);
		return;
	    } 

	    logger.info("ECS service handling is enabled");
	}

	logger.info("Checking for server inactivity with delay: " + delay + " minutes");
	playerCountExecutor = Executors.newSingleThreadScheduledExecutor(playerCountThreadFactory);
        playerCountExecutor.scheduleWithFixedDelay(new PlayerCountRunnable(logger, ecsEnabled), delay, delay, TimeUnit.MINUTES);
    }

    @Override
    public void onDisable() {
	if (playerCountExecutor != null) {
	    playerCountExecutor.shutdown();
	}

        EcsClientHelper.closeClient();
    }
}

