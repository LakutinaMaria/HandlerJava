package com.handler.handler;

public interface HandlerInterface {
    class Response {
        public enum Result {Success, RetryAfter, Failure}

        public final Result result;
        public final String applicationId;
        public final String applicationStatus;
        public final long delay;

        public Response(Result result, String applicationId, String applicationStatus, long delay) {
            this.result = result;
            this.applicationId = applicationId;
            this.applicationStatus = applicationStatus;
            this.delay = delay;
        }
    }

    class ApplicationStatusResponse {
        public enum Result {Success, Failure}

        public final Result result;
        public final String id;
        public final String status;
        public final long lastRequestTime;
        public final int retriesCount;

        public ApplicationStatusResponse(Result result, String id, String status, long lastRequestTime, int retriesCount) {
            this.result = result;
            this.id = id;
            this.status = status;
            this.lastRequestTime = lastRequestTime;
            this.retriesCount = retriesCount;
        }
    }

    ApplicationStatusResponse performOperation(String applicationId) throws InterruptedException;

}
