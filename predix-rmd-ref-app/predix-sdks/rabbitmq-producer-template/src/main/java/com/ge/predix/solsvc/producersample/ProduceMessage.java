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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.ge.predix.solsvc.consumersample.handler.SampleCustomObject;

/**
 * 
 * @author 212367843
 */
@PropertySource("classpath:application.properties")
public class ProduceMessage
{
    private static final Logger logger = LoggerFactory.getLogger(ProduceMessage.class.getName());
    
    @Autowired
    private RabbitTemplate myEventTemplate;

    @Autowired
    private MessageConverter ctdMessageConverter;
    
    @Value("${fieldChangedEvent.MainQueue}")
    private String mainQ;
    
    /* (non-Javadoc)
     * @see com.ge.predix.dispatcherqproducer.api.produceFieldChangedEvent#produceFieldChangedEvent(com.ge.dsp.pm.solution.service.fieldchanged.FieldChangedEvent)
     */
    public boolean produceStringMessage(String data)
    {

        logger.debug("In produceStringMessage......");
        
    	MessageProperties prop = new MessageProperties();
    	prop.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
    	
        Message msg = ctdMessageConverter.toMessage(data, prop);
        
        logger.debug("publishing string to ......= " + mainQ);
        myEventTemplate.convertAndSend(mainQ, msg);
        
        return true;
    }
    
    public boolean produceJsonMessage(SampleCustomObject data)
    {

        logger.debug("In produceJsonMessage......");
        
    	MessageProperties prop = new MessageProperties();
    	prop.setContentType(MessageProperties.CONTENT_TYPE_JSON);
    	
        Message msg = ctdMessageConverter.toMessage(data, prop);
        
        logger.debug("publishing json to ......= " + mainQ);
        myEventTemplate.convertAndSend(mainQ, msg);
        
        return true;
    }

    public boolean produceXMLMessage(SampleCustomObject data)
    {

        logger.debug("In produceXmlMessage......");
        
    	MessageProperties prop = new MessageProperties();
    	prop.setContentType(MessageProperties.CONTENT_TYPE_XML);
    	
        Message msg = ctdMessageConverter.toMessage(data, prop);
        
        logger.debug("publishing xml object to ......= " + mainQ);
        myEventTemplate.convertAndSend(mainQ, msg);
        
        return true;
    }
    
}
