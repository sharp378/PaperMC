package com.github.sharp378.paper.servinator.helpers;

import com.github.sharp378.paper.servinator.constants.Env;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.EcsException;
import software.amazon.awssdk.services.ecs.model.UpdateServiceRequest;

// TODO - may want to look into async where applicable
public class EcsClientHelper {
    
    private static EcsClient ecsClient;

    public static void initClient() {
        if (ecsClient != null) {
	    return;
	}
	
	ecsClient = EcsClient.create();
    }

    public static EcsClient getClient() {
	return ecsClient;
    }

    public static void closeClient() {
	if (ecsClient == null) {
	    return;
	}

    	ecsClient.close();
    }

    public static void updateService(UpdateServiceRequest.Builder requestBuilder) {
	UpdateServiceRequest initialRequest = requestBuilder.build();
        
	// set default request values
	if (initialRequest.cluster() == null) {
	    requestBuilder.cluster(Env.ECS_CLUSTER_ARN);
	}

	if (initialRequest.service() == null) {
	    requestBuilder.service(Env.ECS_SERVICE_ARN);
	}

	UpdateServiceRequest request = requestBuilder.build();
	ecsClient.updateService(request);
    }

}
