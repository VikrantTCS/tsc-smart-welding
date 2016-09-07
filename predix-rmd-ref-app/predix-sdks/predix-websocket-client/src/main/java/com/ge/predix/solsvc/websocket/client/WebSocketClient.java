/*
 * Copyright (c) 2016 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.predix.solsvc.websocket.client;

import java.io.IOException;
import java.util.List;

import com.ge.predix.solsvc.websocket.config.IWebSocketConfig;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;

/**
 * 
 * @author 212438846
 */
public interface WebSocketClient
{

    /**
     * @param config - use implemented IWebSocketConfig
     */
    public void init(IWebSocketConfig config);

    /**
     * @param ws - WebSocket Instance
     * @param text - input type
     * @param listener -
     * @throws IOException -
     * @throws WebSocketException -
     */
    public abstract void postTextWSData(String text, WebSocketAdapter listener)
            throws IOException, WebSocketException;

    /**
     * @param ws - WebSocket Instance
     * @param textList - input type
     * @param listener -
     * @throws IOException -
     * @throws WebSocketException -
     */
    public abstract void postTextArrayWSData(List<String> textList, WebSocketAdapter listener)
            throws IOException, WebSocketException;

    /**
     * @param ws - WebSocket Instance
     * @param bytes - input type
     * @param listener -
     * @throws IOException -
     * @throws WebSocketException -
     */
    public abstract void postBinaryWSData(byte[] bytes, WebSocketAdapter listener)
            throws IOException, WebSocketException;

}
