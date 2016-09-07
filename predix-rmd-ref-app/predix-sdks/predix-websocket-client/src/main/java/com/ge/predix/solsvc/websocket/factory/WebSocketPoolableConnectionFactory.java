package com.ge.predix.solsvc.websocket.factory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ge.predix.solsvc.restclient.impl.RestClient;
import com.ge.predix.solsvc.websocket.config.IWebSocketConfig;
import com.neovisionaries.ws.client.ProxySettings;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

/**
 * 
 * @author 212438846 -
 */
@Component
public class WebSocketPoolableConnectionFactory extends BasePoolableObjectFactory
{

    /**
     * 
     */
    // not declared as private--emulated by a synthetic accessor method warning
    static Logger            log      = LoggerFactory.getLogger(WebSocketPoolableConnectionFactory.class);

    /**
     * 
     */
    @Autowired
    private RestClient       restClient;

    /**
     * 
     */
    private List<Header>     headers;

    /**
     * Factory for Creating WebSocket
     */
    private WebSocketFactory factory;

    /**
     * 
     */
    private IWebSocketConfig config;

    /**
     * 
     */
    // callback to know if connection was successful
    private WebSocketAdapter listener = new WebSocketAdapter()
                                      {
                                          @Override
                                          public void onConnected(WebSocket websocket,
                                                  Map<String, List<String>> wsHeaders)
                                          {
                                              log.debug("CONNECTED...." + wsHeaders.toString());              //$NON-NLS-1$
                                          }
                                      };

    /**
     * @param wsConfig -
     */
    public void init(IWebSocketConfig wsConfig)
    {
        this.config = wsConfig;
        initHeaders();
    }

    private void initHeaders()
    {
        // headers required for authentication and for predix service
        this.headers = this.restClient.getSecureTokenForClientId();
        this.headers.add(new BasicHeader("Predix-Zone-Id", this.config.getZoneId())); //$NON-NLS-1$
        // Origin header required as it is not being set by the websocket
        this.headers.add(new BasicHeader("Origin", "http://localhost")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public Object makeObject()
            throws IOException, WebSocketException
    {
        WebSocket ws = connect();
        return ws;
    }

    @Override
    public boolean validateObject(Object obj)
    {
        if ( obj instanceof WebSocket )
        {
            if ( ((WebSocket) obj).isOpen() )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @param wsUri - Websocket uri to connect to
     * @param headers - user defined headers
     * @return WebSocket
     * @throws IOException -
     * @throws WebSocketException -
     */
    public WebSocket connect()
            throws IOException, WebSocketException
    {

        this.factory = new WebSocketFactory();
        detectAndSetProxy();

        WebSocket ws = this.factory.createSocket(this.config.getWsUri());
        setUserDefinedHeaders(ws, this.headers);
        ws.addListener(this.listener);

        ws.connect();
        return ws;
    }

    private void detectAndSetProxy()
    {
        // setting proxies for websocket
        if ( !StringUtils.isEmpty(this.config.getWsProxyHost()) && !StringUtils.isEmpty(this.config.getWsProxyPort()) )
        {
            ProxySettings settings = this.factory.getProxySettings();
            settings.setServer("http://" + this.config.getWsProxyHost() + ":" + this.config.getWsProxyPort()); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * @param headers
     */
    private void setUserDefinedHeaders(WebSocket ws, List<Header> headers)
    {
        // setting user provided headers
        for (Header header : headers)
        {
            ws.addHeader(header.getName(), header.getValue());
        }
    }
}
