package com.github.sharp378.paper.servinator.runnables;

import org.bukkit.Bukkit;

public class PlayerCountRunnable implements Runnable {

    private static boolean isPlayerOnline() {
        return (Bukkit.getOnlinePlayers()).size() != 0;
    }

    @Override
    public void run() {
	if (!isPlayerOnline()) {
	    Bukkit.shutdown();
	}
    }
}

