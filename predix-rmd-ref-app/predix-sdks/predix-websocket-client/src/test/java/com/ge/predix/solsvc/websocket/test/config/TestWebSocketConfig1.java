package com.ge.predix.solsvc.websocket.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ge.predix.solsvc.websocket.config.IWebSocketConfig;

/**
 * 
 * @author 212438846
 * Class TestWebSocketConfig1 implements IWebSocketConfig. Every separate websocket server connection requires the
 * IWebSocketConfig interface to be implemented per connection.   
 */
@Component
public class TestWebSocketConfig1
        implements IWebSocketConfig
{

    @Value("${predix.oauth.proxyHost1:}")
    private String wsProxyHost;

    @Value("${predix.oauth.proxyPort1:}")
    private String wsProxyPort;

    @Value("${predix.websocket.uri1}")
    private String wsUri;

    @Value("${predix.websocket.zoneid1}")
    private String zoneId;

    @Value("${predix.websocket.pool.maxIdle1:}")
    private int    wsMaxIdle;

    @Value("${predix.websocket.pool.maxActive1:}")
    private int    wsMaxActive;

    @Value("${predix.websocket.pool.maxWait1}")
    private int    wsMaxWait;

    /*
     * (non-Javadoc)
     * @see com.ge.predix.solsvc.websocket.config.IWebSocketConfig#getWsMaxIdle()
     */
    @Override
    public int getWsMaxIdle()
    {
        return this.wsMaxIdle;
    }



    /*
     * (non-Javadoc)
     * @see com.ge.predix.solsvc.websocket.config.IWebSocketConfig#getWsMaxActive()
     */
    @Override
    public int getWsMaxActive()
    {
        return this.wsMaxActive;
    }


    /*
     * (non-Javadoc)
     * @see com.ge.predix.solsvc.websocket.config.IWebSocketConfig#getWsMaxWait()
     */
    @Override
    public int getWsMaxWait()
    {
        return this.wsMaxWait;
    }


    /*
     * (non-Javadoc)
     * @see com.ge.predix.solsvc.websocket.config.IWebSocketConfig#getWsProxyHost()
     */
    @Override
    public String getWsProxyHost()
    {
        return this.wsProxyHost;
    }


    /*
     * (non-Javadoc)
     * @see com.ge.predix.solsvc.websocket.config.IWebSocketConfig#getWsProxyPort()
     */
    @Override
    public String getWsProxyPort()
    {
        return this.wsProxyPort;
    }


    /*
     * (non-Javadoc)
     * @see com.ge.predix.solsvc.websocket.config.IWebSocketConfig#getWsUri()
     */
    @Override
    public String getWsUri()
    {
        return this.wsUri;
    }


    /*
     * (non-Javadoc)
     * @see com.ge.predix.solsvc.websocket.config.IWebSocketConfig#getZoneId()
     */
    @Override
    public String getZoneId()
    {
        return this.zoneId;
    }

}
