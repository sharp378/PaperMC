package com.github.sharp378.paper.servinator.helpers;

import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.EcsException;
import software.amazon.awssdk.services.ecs.model.UpdateServiceRequest;

// TODO - may want to look into async where applicable
public class EcsClientHelper {
    
    private static EcsClient ecsClient;

    private static void initClient() {
        ecsClient = EcsClient.create();
    }

    public static EcsClient getClient() {
    	if (ecsClient == null) {
	    initClient();
	}
	
	return ecsClient;
    }

    public static String getClientServiceArn() {
        final String serviceName = ""; //ecsClient.getServiceName();

	// TODO - implement
	return serviceName;
    }

    public static void closeClient() {
	if (ecsClient == null) {
	    return;
	}

    	ecsClient.close();
    }

    public static void updateSpecificService(UpdateServiceRequest request) {
        ecsClient.updateService(request);
    }

}
