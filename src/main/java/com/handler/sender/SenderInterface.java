package com.handler.sender;

import java.time.Duration;
import java.util.List;

public interface SenderInterface {

    record Payload(String origin, byte[] data) {}
    record Address(String datacenter, String nodeId) {}
    record Event(List<Address> recipients, Payload payload) {
        public List<Address> getRecipients() {
            return this.recipients;
        }

        public Payload getPayload(){
            return this.payload;
        }
    }

    enum Result { ACCEPTED, REJECTED }

    Duration timeout();

    void performOperation();
}
