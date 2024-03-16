package com.github.sharp378.paper.servinator.constants;

import java.lang.System;

public interface Env {
    public static final long PLUGIN_DELAY = Long.parseLong(System.getenv("PLUGIN_DELAY"));
    public static final boolean ECS_ENABLED = Boolean.parseBoolean(System.getenv("ECS_ENABLED"));
    public static final String ECS_CLUSTER_ARN = System.getenv("ECS_CLUSTER_ARN");
    public static final String ECS_SERVICE_ARN = System.getenv("ECS_SERVICE_ARN");
}

