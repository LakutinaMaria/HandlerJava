package com.handler.handler;

import java.util.concurrent.*;

public class Handler implements HandlerInterface {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    public ApplicationStatusResponse performOperation(String applicationId) throws InterruptedException {
        CompletableFuture<Response> service1Future = CompletableFuture.supplyAsync(() -> getServiceResponse(applicationId), executorService);
        CompletableFuture<Response> service2Future = CompletableFuture.supplyAsync(() -> getServiceResponse(applicationId), executorService);

        CompletableFuture<Response> combinedFuture = service1Future.applyToEither(service2Future, response -> response);

        try {
            Response response = combinedFuture.get(15, TimeUnit.SECOND);
            return createApplicationStatusResponse(response);
        } catch (TimeoutException | ExecutionException e) {
            return new ApplicationStatusResponse(ApplicationStatusResponse.Result.Failure, "", "", 0, 2);
        } finally {
            executorService.shutdown();
        }
    }

    private Response getServiceResponse(String applicationId) {
        // A call to the service
        return new Response(Response.Result.Success, applicationId, "Approved", 0);
    }

    private ApplicationStatusResponse createApplicationStatusResponse(Response response) {
        if (response.result == Response.Result.Success) {
            return new ApplicationStatusResponse(ApplicationStatusResponse.Result.Success,
                    response.applicationId, response.applicationStatus, 0, 0);
        } else {
            return new ApplicationStatusResponse(ApplicationStatusResponse.Result.Failure,
                    response.applicationId, response.applicationStatus, System.currentTimeMillis(), 1);
        }
    }
}
