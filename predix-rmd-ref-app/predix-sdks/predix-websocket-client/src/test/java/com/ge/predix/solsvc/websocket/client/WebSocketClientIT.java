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

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ge.predix.solsvc.websocket.test.config.TestWebSocketConfig1;
import com.ge.predix.solsvc.websocket.test.config.TestWebSocketConfig2;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;

/**
 * 
 * @author 212438846
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("com.ge.predix.solsvc.restclient")
@ActiveProfiles("local")
@ContextConfiguration(locations =
{
        "classpath*:META-INF/spring/predix-websocket-client-scan-context.xml",
        "classpath*:META-INF/spring/predix-test-websocket-client-scan-context.xml",
        "classpath*:META-INF/spring/predix-rest-client-scan-context.xml",
        "classpath*:META-INF/spring/predix-rest-client-sb-properties-context.xml"
})
public class WebSocketClientIT
{

    /**
     * 
     */
    // not declared as private--emulated by a synthetic accessor method warning
    static Logger            log             = LoggerFactory.getLogger(WebSocketClientIT.class);

    /**
     * 
     */
    @Autowired
    TestWebSocketConfig1     testWebSocketConfig1;

    /**
     * 
     */
    @Autowired
    TestWebSocketConfig2     testWebSocketConfig2;

    @Autowired
    private WebSocketClient  client1;

    @Autowired
    private WebSocketClient  client2;

    /**
     * 
     */
    private WebSocketAdapter messageListener = new WebSocketAdapter()
                                             {
                                                 @Override
                                                 public void onTextMessage(WebSocket wsocket, String message)
                                                 {
                                                     log.debug("RECEIVED...." + message);                      //$NON-NLS-1$
                                                 }

                                                 @Override
                                                 public void onBinaryMessage(WebSocket wsocket, byte[] binary)
                                                 {
                                                     String str = new String(binary, StandardCharsets.UTF_8);
                                                     log.debug("RECEIVED...." + str);                          //$NON-NLS-1$
                                                 }
                                             };

    /**
     * 
     */
    @Before
    public void setup()
    {
        this.client1.init(this.testWebSocketConfig1);
        this.client2.init(this.testWebSocketConfig2);
    }

    /**
     * 
     */
    @Test
    public void postDataTest()
    {

        try
        {

            String testMessage1 = "{\"messageId\": \"1453338376222\",\"body\": [{\"name\": \"Compressor-2015:CompressionRatio\",\"datapoints\": [[1453338376222,10,3],[1453338376222,10,1]],\"attributes\": {\"host\": \"server1\",\"customer\": \"Acme1\"}}]}"; //$NON-NLS-1$
            this.client1.postTextWSData(testMessage1, this.messageListener);
            this.client2.postTextWSData(testMessage1, this.messageListener);
            String testMessage2 = "{\"messageId\": \"1453338376223\",\"body\": [{\"name\": \"Compressor-2016:CompressionRatio\",\"datapoints\": [[1453338376223,10,3],[1453338376223,10,1]],\"attributes\": {\"host\": \"server2\",\"customer\": \"Acme2\"}}]}"; //$NON-NLS-1$
            this.client1.postTextWSData(testMessage2, this.messageListener);
            this.client2.postTextWSData(testMessage2, this.messageListener);

            // post data as text List
            this.client1.postTextArrayWSData(generateTextArray(), this.messageListener);
            this.client2.postTextArrayWSData(generateTextArray(), this.messageListener);
            // port data as binary array
            byte[] byteArray = generateBinaryArray();
            if ( byteArray != null && byteArray.length != 0 )
            {
                this.client1.postBinaryWSData(byteArray, this.messageListener);
                this.client2.postBinaryWSData(byteArray, this.messageListener);
            }

        }
        catch (IOException e)
        {
            fail("Failed to connect to WS due to IOException." + e.getMessage()); //$NON-NLS-1$
        }
        catch (WebSocketException e)
        {
            fail("Failed to connect to WS due to WebSocketException." + e.getMessage()); //$NON-NLS-1$
        }

        try
        {// wait added for time delay in callback from websocket endpoint
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            fail("Failed due to thread interruption." + e.getMessage()); //$NON-NLS-1$
        }
    }

    /**
     * 
     */
    @Test
    public void postDataTestSlightLoad()
    {
        try
        {
            for (int i = 0; i < 25; i++)
            {
                // post data multiple times
                String testMessage1 = "{\"messageId\": \"1453338376222\",\"body\": [{\"name\": \"Compressor-2015:CompressionRatio\",\"datapoints\": [[1453338376222,10,3],[1453338376222,10,1]],\"attributes\": {\"host\": \"server1\",\"customer\": \"Acme1\"}}]}"; //$NON-NLS-1$
                this.client2.postTextWSData(testMessage1, this.messageListener);
                String testMessage2 = "{\"messageId\": \"1453338376223\",\"body\": [{\"name\": \"Compressor-2016:CompressionRatio\",\"datapoints\": [[1453338376223,10,3],[1453338376223,10,1]],\"attributes\": {\"host\": \"server2\",\"customer\": \"Acme2\"}}]}"; //$NON-NLS-1$
                this.client2.postTextWSData(testMessage2, this.messageListener);

                // post data as text List
                this.client2.postTextArrayWSData(generateTextArray(), this.messageListener);

                // port data as binary array
                byte[] byteArray = generateBinaryArray();
                if ( byteArray != null && byteArray.length != 0 )
                {
                    this.client2.postBinaryWSData(byteArray, this.messageListener);
                }
            }

        }
        catch (IOException e)
        {
            fail("Failed to connect to WS due to IOException." + e.getMessage()); //$NON-NLS-1$
        }
        catch (WebSocketException e)
        {
            fail("Failed to connect to WS due to WebSocketException." + e.getMessage()); //$NON-NLS-1$
        }

        try
        {// wait added for time delay in callback from websocket endpoint
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            fail("Failed due to thread interruption." + e.getMessage()); //$NON-NLS-1$
        }
    }

    private List<String> generateTextArray()
    {
        String testMessage1 = "{\"messageId\": \"1453338376210\",\"body\": [{\"name\": \"Compressor-2010:CompressionRatio\",\"datapoints\": [[1453338376223,10,3],[1453338376223,10,1]],\"attributes\": {\"host\": \"server2\",\"customer\": \"Acme2\"}}]}"; //$NON-NLS-1$
        String testMessage2 = "{\"messageId\": \"1453338376211\",\"body\": [{\"name\": \"Compressor-2011:CompressionRatio\",\"datapoints\": [[1453338376223,10,3],[1453338376223,10,1]],\"attributes\": {\"host\": \"server2\",\"customer\": \"Acme2\"}}]}"; //$NON-NLS-1$
        String testMessage3 = "{\"messageId\": \"1453338376212\",\"body\": [{\"name\": \"Compressor-2012:CompressionRatio\",\"datapoints\": [[1453338376223,10,3],[1453338376223,10,1]],\"attributes\": {\"host\": \"server2\",\"customer\": \"Acme2\"}}]}"; //$NON-NLS-1$
        String testMessage4 = "{\"messageId\": \"1453338376213\",\"body\": [{\"name\": \"Compressor-2013:CompressionRatio\",\"datapoints\": [[1453338376223,10,3],[1453338376223,10,1]],\"attributes\": {\"host\": \"server2\",\"customer\": \"Acme2\"}}]}"; //$NON-NLS-1$
        String testMessage5 = "{\"messageId\": \"1453338376214\",\"body\": [{\"name\": \"Compressor-2014:CompressionRatio\",\"datapoints\": [[1453338376223,10,3],[1453338376223,10,1]],\"attributes\": {\"host\": \"server2\",\"customer\": \"Acme2\"}}]}"; //$NON-NLS-1$

        List<String> textList = new ArrayList<String>();
        textList.add(testMessage1);
        textList.add(testMessage2);
        textList.add(testMessage3);
        textList.add(testMessage4);
        textList.add(testMessage5);

        return textList;
    }

    private byte[] generateBinaryArray()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("testData/LargeTimeSeriesData.txt").getFile()); //$NON-NLS-1$
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try
        {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            try
            {
                ios = new FileInputStream(file);
            }
            catch (FileNotFoundException e)
            {
                throw new RuntimeException(e);
            }
            int read = 0;
            try
            {
                while ((read = ios.read(buffer)) != -1)
                {
                    ous.write(buffer, 0, read);
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        finally
        {
            try
            {
                if ( ous != null ) ous.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            try
            {
                if ( ios != null ) ios.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        return (ous != null) ? ous.toByteArray() : null;
    }

}
