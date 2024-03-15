package com.github.sharp378.paper.servinator;

import com.github.sharp378.paper.servinator.runnables.*;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.*;

public class ServinatorPlugin extends JavaPlugin {
    
    private final static ThreadFactory playerCountThreadFactory = new ThreadFactoryBuilder()
        .setNameFormat("PlayerCount")
        .build();

    private ScheduledExecutorService playerCountExecutor;

    @Override
    public void onEnable() {
	saveDefaultConfig();
	
	long delay = getConfig().getLong("delay", 5); 
	getLogger().info("Checking for server inactivity with delay: " + delay + " minutes");
	playerCountExecutor = Executors.newSingleThreadScheduledExecutor(playerCountThreadFactory);
        playerCountExecutor.scheduleWithFixedDelay(new PlayerCountRunnable(), delay, delay, TimeUnit.MINUTES);

	// TODO - ecs kill if possible
    }

    @Override
    public void onDisable() {
	if (playerCountExecutor != null) {
	  playerCountExecutor.shutdown();
	}
    }
}

