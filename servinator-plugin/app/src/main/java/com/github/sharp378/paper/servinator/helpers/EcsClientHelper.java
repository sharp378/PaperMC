package com.github.sharp378.paper.servinator.helpers;

import com.github.sharp378.paper.servinator.constants.Env;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.EcsException;
import software.amazon.awssdk.services.ecs.model.UpdateServiceRequest;

import java.util.function.Consumer;

// TODO - may want to look into async where applicable
public class EcsClientHelper {
    
    private static EcsClient ecsClient;
    private static UpdateServiceRequest.Builder baseRequestBuilder;
    
    public static void initClient() {
        if (ecsClient != null) {
	    return;
	}
	
	ecsClient = EcsClient.create();
        baseRequestBuilder = UpdateServiceRequest.builder()
	    .cluster(Env.ECS_CLUSTER_ARN)
	    .service(Env.ECS_SERVICE_ARN);
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

    public static void updateSpecificService(Consumer<UpdateServiceRequest.Builder> requestConsumer) {
        UpdateServiceRequest request = baseRequestBuilder
            .applyMutation(requestConsumer)
	    .build();

	ecsClient.updateService(request);
    }

}
