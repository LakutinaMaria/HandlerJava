package com.handler.sender;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Sender implements SenderInterface {


    private final Client client;
    private final ExecutorService executorService;

    public Sender(Client client) {
        this.client = client;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public Sender(Client client, ExecutorService executorService) {
        this.client = client;
        this.executorService = executorService;
    }

    @Override
    public Duration timeout() {
        return Duration.ofMillis(100);
    }

    @Override
    public void performOperation() {
        while (true) {
            Event event = client.readData();

            for (Address recipient : event.getRecipients()) {
                executorService.submit(() -> {
                    Result result = Result.REJECTED;
                    while (result == Result.REJECTED) {
                        result = client.sendData(recipient, event.getPayload());
                        if (result == Result.REJECTED) {
                            try {
                                TimeUnit.MILLISECONDS.sleep(timeout().toMillis());
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                });
            }
        }
    }
}


