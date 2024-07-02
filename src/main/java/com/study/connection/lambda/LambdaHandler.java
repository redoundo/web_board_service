package com.study.connection.lambda;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.study.connection.Application;

public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    private static long codeDownloadStartTime;
    static {
        codeDownloadStartTime = System.currentTimeMillis();
    }
    private static long containerStartTime;
    private static long runtimeStartTime;
    private static boolean isColdStart = true;


    public LambdaHandler()  {
        if(isColdStart) {
            containerStartTime = System.currentTimeMillis();
            isColdStart = false;
        }
        try{
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class);
        } catch (ContainerInitializationException e){
            throw new RuntimeException("Unable to Start Spring Boot Application", e);
        }
    }

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
        if(runtimeStartTime ==0) runtimeStartTime = System.currentTimeMillis();

        long executionStartTime = System.currentTimeMillis();
        AwsProxyResponse response = handler.proxy(input, context);
        long executionEndTime = System.currentTimeMillis();

        long codeDownloadDuration = containerStartTime - codeDownloadStartTime;
        context.getLogger().log("codeDownloadDuration:   " + codeDownloadDuration);
        long containerDuration = runtimeStartTime - containerStartTime;
        context.getLogger().log("containerDuration:   " + containerDuration);
        long runtimeDuration = executionStartTime - runtimeStartTime;
        context.getLogger().log("runtimeDuration:   " + runtimeDuration);
        long executionDuration = executionEndTime - executionStartTime;
        context.getLogger().log("executionDuration:   " + executionDuration);

        return response;
    }
}
