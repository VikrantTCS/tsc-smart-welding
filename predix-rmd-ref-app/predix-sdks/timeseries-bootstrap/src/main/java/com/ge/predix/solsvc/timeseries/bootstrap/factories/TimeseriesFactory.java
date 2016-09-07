/*
 * Copyright (c) 2015 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.predix.solsvc.timeseries.bootstrap.factories;

import java.util.List;

import org.apache.http.Header;

import com.ge.predix.entity.timeseries.aggregations.AggregationsList;
import com.ge.predix.entity.timeseries.datapoints.ingestionrequest.DatapointsIngestion;
import com.ge.predix.entity.timeseries.datapoints.queryrequest.DatapointsQuery;
import com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.DatapointsLatestQuery;
import com.ge.predix.entity.timeseries.datapoints.queryresponse.DatapointsResponse;
import com.ge.predix.entity.timeseries.tags.TagsList;
import com.neovisionaries.ws.client.WebSocketAdapter;

/**
 * The main entry point for using the Timeseries Bootstrap. Each method represents a major Time Series API.
 * 
 * @author 212438846
 *
 */
public interface TimeseriesFactory
{
	
	/**
	 * @param messageListener - method accepts custom message listener
	 * @since Predix Timeseries API v1.0 Method to create connection to TS
	 *        Websocket to the configured TS Server List<Header> headers	  
	 */
	public void createConnectionToTimeseriesWebsocket(WebSocketAdapter messageListener);

    /**
     * @since Predix Timeseries API v1.0
     *        Method to connect to TS Websocket to the configured TS Server
     * 		  List<Header> headers       
     * 
     */
    public void  createConnectionToTimeseriesWebsocket();
    
    /**
	 * @since Predix Timeseries API v1.0 Method to post data through Websocket
	 *        to the configured TS Server
	 * @param datapointsIngestion -
	 *            @see DatapointsIngestion
	 * 
	 */	
	public void postDataToTimeseriesWebsocket(DatapointsIngestion datapointsIngestion);

    /**
     * @since Predix Timeseries API v1.0
     * @param uri @see TimeSeriesAPIV1
     * @param datapointsQuery @see DatapointsQuery
     * @param headers {@href https://github.com/PredixDev/predix-rest-client}
     * @return @see DatapointsResponse
     */
		
    /**
     * @param uri  -
     * @since Predix Timeseries API v1.0
     * @param baseUrl @see TimeSeriesAPIV1
     * @param DatapointsQuery @see DatapointsQuery
     * @param headers {@href https://github.com/PredixDev/predix-rmd-ref-app}
     * @return @see DatapointsResponse
     */
    public DatapointsResponse queryForDatapoints(String uri, DatapointsQuery DatapointsQuery, List<Header> headers);

    /**
     * @since Predix Timeseries API v1.0
     * @param baseUrl @see TimeSeriesAPIV1
     * @param latestDatapoints @see DatapointsLatestQuery
     * @param headers {@href https://github.com/PredixDev/predix-rmd-ref-app}
     * @return @see DatapointsResponse
     */

    public DatapointsResponse queryForLatestDatapoint(String baseUrl, DatapointsLatestQuery latestDatapoints,
            List<Header> headers);
        
    /**
     * @since Predix Timeseries API v1.0
     * @param baseUrl @see TimeSeriesAPIV1
       * @param headers {@href https://github.com/PredixDev/predix-rmd-ref-app}
     * @return @see TagsList
     */
    public TagsList listTags(String baseUrl, List<Header> headers);
    
    /**
     * @since Predix Timeseries API v1.0
     * @param baseUrl @see TimeSeriesAPIV1
       * @param headers {@href https://github.com/PredixDev/predix-rmd-ref-app}
     * @return @see AggregationsList
     */
    public AggregationsList listAggregations(String baseUrl, List<Header> headers);
  	
}
