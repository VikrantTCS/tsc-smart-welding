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

import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ge.predix.solsvc.websocket.config.IWebSocketConfig;
import com.ge.predix.solsvc.websocket.factory.WebSocketPoolableConnectionFactory;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;

/**
 * 
 * @author 212438846
 */
@Component
public class WebSocketClientImpl
        implements WebSocketClient
{

    /**
	 * 
	 */
    private static Logger log    = LoggerFactory.getLogger(WebSocketClientImpl.class);
    
    private boolean isInitialized = false;

    @Autowired
    private WebSocketPoolableConnectionFactory webSocketPoolableConnectionFactory;

    private static final GenericObjectPool     wsPool = new GenericObjectPool();
    

    /**
     * Method to initialize the WS pool
     */
    @SuppressWarnings(
    {
       "nls"
    })
    @Override
    public void init(IWebSocketConfig config)
    {
        wsPool.setMaxActive(config.getWsMaxActive());
        wsPool.setMaxIdle(config.getWsMaxIdle());
        wsPool.setMaxWait(config.getWsMaxWait());
        this.webSocketPoolableConnectionFactory.init(config);
        wsPool.setFactory(this.webSocketPoolableConnectionFactory);
        for (int i = 0; i < wsPool.getMaxActive(); ++i)
        {
            try
            {
                wsPool.addObject();
            }
            catch (Exception e)
            {
                log.error("Encountered issue creating WebSocket in pool. " + e);
                throw new RuntimeException("Encountered issue creating WebSocket in pool. ", e);
            }
        }
        this.isInitialized = true;
    }

    /**
     * Method that gets a websocket instance
     */
    private WebSocket borrowWSfromPool()
    {
        try
        {
            return (WebSocket) wsPool.borrowObject();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to get WebSocket from pool", e); //$NON-NLS-1$
        }
    }

    /**
     * Method that returns a websocket instance
     */
    private void validateAndReturnWStoPool(WebSocket ws)
    {
        try
        {
            wsPool.returnObject(ws);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to return websocket to the pool ", e); //$NON-NLS-1$
        }
    }

    /**
     * @param ws - WebSocket Instance
     * @param text  - input type
     */

    @SuppressWarnings("nls")
    @Override
    public void postTextWSData(String text, WebSocketAdapter listener)
    {
        if(!this.isInitialized){
            throw new UnsupportedOperationException("WebSocket Pool has not been initialized. Call init method");
        }
        WebSocket ws = borrowWSfromPool();
        try
        {
            checkWebSocketConnection(ws);
            ws.addListener(listener);
            ws.sendText(text);
        }
        catch (IOException e)
        {
            throw new RuntimeException("unable to complete task ws=" + ws.getURI(), e);
        }
        finally
        {
            // return connection to pool
            validateAndReturnWStoPool(ws);
        }
    }

    /**
     * @param ws - WebSocket Instance
     * @param textList  - input type
     *             
     */
    @SuppressWarnings("nls")
    @Override
    public void postTextArrayWSData(List<String> textList, WebSocketAdapter listener)
    {
        if(!this.isInitialized){
            throw new UnsupportedOperationException("WebSocket Pool has not been initialized. Call init method");
        }
        WebSocket ws = borrowWSfromPool();
        try
        {
            checkWebSocketConnection(ws);
            // callback to know if text message sent was successful
            
            ws.addListener(listener);
            for (String text : textList)
            {
                ws.sendText(text);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("unable to complete task ws=" + ws.getURI(), e);
        }
        finally
        {
            // return connection to pool
            validateAndReturnWStoPool(ws);
        }
    }

    /**
     * @param ws  - WebSocket Instance
     * @param bytes - input type
     */
    @SuppressWarnings("nls")
    @Override
    public void postBinaryWSData(byte[] bytes, WebSocketAdapter listener)
    {
        if(!this.isInitialized){
            throw new UnsupportedOperationException("WebSocket Pool has not been initialized. Call init method");
        }
        WebSocket ws = borrowWSfromPool();
        try
        {
            checkWebSocketConnection(ws);
            
            ws.addListener(listener);
            ws.sendBinary(bytes);
        }
        catch (IOException e)
        {
            throw new RuntimeException("unable to complete task ws=" + ws.getURI(), e);
        }
        finally
        {
            // return connection to pool
            validateAndReturnWStoPool(ws);
        }
    }


    /**
     * @param ws
     * @throws IOException
     */
    private void checkWebSocketConnection(WebSocket ws)
            throws IOException
    {
        if ( ws == null || !ws.isOpen() )
        {
            log.error("Websocket Connection is NOT OPEN"); //$NON-NLS-1$
            throw new IOException("Websocket Connection is NOT OPEN"); //$NON-NLS-1$
        }
    }
}
