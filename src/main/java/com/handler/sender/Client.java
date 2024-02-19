package com.handler.sender;


public interface Client {

    SenderInterface.Event readData();

    SenderInterface.Result sendData(SenderInterface.Address dest, SenderInterface.Payload payload);
}
