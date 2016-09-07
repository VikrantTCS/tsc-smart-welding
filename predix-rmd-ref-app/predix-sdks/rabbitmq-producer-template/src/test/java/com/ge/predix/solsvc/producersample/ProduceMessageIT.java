/*
 * Copyright (c) 2015 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
 
package com.ge.predix.solsvc.producersample;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ge.predix.solsvc.consumersample.handler.SampleCustomObject;

/**
 * 
 * @author 212367843
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/rabbitmq-producer-test.xml")
public class ProduceMessageIT
{    
    @Autowired
    private ProduceMessage produceMsg;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass()
            throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass()
            throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp()
            throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown()
            throws Exception
    {
    }

    @Test
    public void testProduceStringMessage()
    {
        //System.out.println("Starting test...producing string");
        //System.out.println("=======================================");
        
        produceMsg.produceStringMessage("Sending string ...test test ..123");
        
    }

    
    @Test
    public void testProduceJsonMessage()
    {
        //System.out.println("Starting test...producing json");
        //System.out.println("=======================================");
        
		SampleCustomObject testObject = new SampleCustomObject();
		testObject.setAttribute1("attrjson1");
		testObject.setAttribute2("attrjson2");
        
        produceMsg.produceJsonMessage(testObject);        
    }
    
    @Test
    public void testProduceXMLMessage()
    {
        //System.out.println("Starting test...producing xml");
        //System.out.println("=======================================");
        
		SampleCustomObject testObject = new SampleCustomObject();
		testObject.setAttribute1("attrxml1");
		testObject.setAttribute2("attrxml2");
        
        produceMsg.produceXMLMessage(testObject);        
    }
}
