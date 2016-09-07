package com.ge.predix.solsvc.dataingestion.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ge.predix.entity.asset.Asset;
import com.ge.predix.entity.asset.AssetTag;
import com.ge.predix.entity.timeseries.datapoints.ingestionrequest.Body;
import com.ge.predix.entity.timeseries.datapoints.ingestionrequest.DatapointsIngestion;
import com.ge.predix.solsvc.dataingestion.api.Constants;
import com.ge.predix.solsvc.dataingestion.service.type.JSONData;
import com.ge.predix.solsvc.dataingestion.websocket.WebSocketConfig;
import com.ge.predix.solsvc.ext.util.JsonMapper;
import com.ge.predix.solsvc.timeseries.bootstrap.config.TimeseriesRestConfig;
import com.ge.predix.solsvc.timeseries.bootstrap.config.TimeseriesWSConfig;
import com.ge.predix.solsvc.timeseries.bootstrap.factories.TimeseriesFactory;
import com.neovisionaries.ws.client.WebSocketException;

/**
 * 
 * @author predix -
 */
@Component
public class TimeSeriesDataIngestionHandler extends BaseFactory {
	private static Logger log = Logger.getLogger(TimeSeriesDataIngestionHandler.class);
	@Autowired
	private TimeseriesFactory timeSeriesFactory;

	@Autowired
	private AssetDataHandler assetDataHandler;

	@Autowired
	private TimeseriesWSConfig tsInjectionWSConfig;

	@Autowired
	private com.ge.predix.solsvc.websocket.client.WebSocketClient wsClient;

	@Autowired
	private JsonMapper jsonMapper;
	
	private Map<String,Asset> assetMap = new HashMap<String,Asset>();

	@Autowired
	private TimeseriesRestConfig timeseriesRestConfig;
	
	@Autowired
	private WebSocketConfig websocketConfig;

	/**
	 * -
	 */
	@SuppressWarnings("nls")
	@PostConstruct
	public void intilizeDataIngestionHandler() {
		this.wsClient.init(this.websocketConfig);
		this.timeSeriesFactory.createConnectionToTimeseriesWebsocket();
		log.info("*******************TimeSeriesDataIngestionHandler Initialization complete*********************");
	}

	/**
	 * @param data -
	 * @param authorization -
	 */
	@SuppressWarnings("nls")
	public void handleData(String data, String authorization) {
		log.info("Data from Simulator : " + data);
		List<Header> headers = this.restClient.getSecureTokenForClientId();
		this.restClient.addZoneToHeaders(headers, this.timeseriesRestConfig.getZoneId());
		headers.add(new BasicHeader("Origin", "http://localhost"));
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<JSONData> list = mapper.readValue(data, new TypeReference<List<JSONData>>() {
				//
			});
			log.debug("TimeSeries URL : " + this.tsInjectionWSConfig.getWsUri());
			

			DatapointsIngestion dpIngestion = createTimeseriesDataBody(list, authorization);
			log.info("TimeSeries JSON : " + this.jsonMapper.toJson(dpIngestion));
			if (dpIngestion.getBody() != null && dpIngestion.getBody().size() > 0) {
				this.wsClient.postTextWSData(this.jsonMapper.toJson(dpIngestion), null);
				log.info("Posted Data to Predix Websocket Server");

				this.timeSeriesFactory.postDataToTimeseriesWebsocket(dpIngestion);
				log.info("Posted Data to Timeseries");
			}

		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (WebSocketException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param json
	 * @param i
	 * @param asset
	 * @param assetTag
	 * @return -
	 */
	@SuppressWarnings({ "unchecked", "unused", "nls" })
	private DatapointsIngestion createTimeseriesDataBody(JSONData json, Long i, Asset asset, AssetTag assetTag) {
		DatapointsIngestion dpIngestion = new DatapointsIngestion();
		dpIngestion.setMessageId(String.valueOf(System.currentTimeMillis()));

		Body body = new Body();
		body.setName(assetTag.getSourceTagId());

		// attributes
		com.ge.predix.entity.util.map.Map map = new com.ge.predix.entity.util.map.Map();
		map.put("assetId", asset.getAssetId());
		if (!StringUtils.isEmpty(assetTag.getSourceTagId())) {
			String sourceTagAttribute = assetTag.getSourceTagId().split(":")[1];
			map.put("sourceTagId", sourceTagAttribute);
		}
		body.setAttributes(map);

		// datapoints
		List<Object> datapoint1 = new ArrayList<Object>();
		datapoint1.add(converLocalTimeToUtcTime(json.getTimestamp().getTime()));
		Double convertedValue = getConvertedValue(assetTag.getTagDatasource().getNodeName(),
				Double.parseDouble(json.getValue().toString()));
		datapoint1.add(convertedValue);

		List<Object> datapoints = new ArrayList<Object>();
		datapoints.add(datapoint1);
		body.setDatapoints(datapoints);

		List<Body> bodies = new ArrayList<Body>();
		bodies.add(body);

		dpIngestion.setBody(bodies);

		return dpIngestion;
	}

	@SuppressWarnings({ "unchecked", "nls" })
	private DatapointsIngestion createTimeseriesDataBody(List<JSONData> jsonData, String authorization) {
		DatapointsIngestion dpIngestion = new DatapointsIngestion();
		dpIngestion.setMessageId(String.valueOf(System.currentTimeMillis()));
		List<Body> bodies = new ArrayList<Body>();

		for (JSONData data : jsonData) {
			String filter = "attributes.machineControllerId.value";
			String value = "/asset/Bently.Nevada.3500.Rack" + data.getUnit();
			String nodeName = data.getName();
			Asset asset = this.assetMap.get(value);
			if (asset == null) {
				asset = this.assetDataHandler.retrieveAsset(null, filter, value, authorization);
				if (asset == null) {
					throw new RuntimeException("Error retriving asset for filter=" + filter + "=" + value); //$NON-NLS-1$
				}
				this.assetMap.put(value, asset);
			}
			if (asset != null) {
				LinkedHashMap<String, AssetTag> tags = asset.getAssetTag();
				if (tags != null) {
					Body body = new Body();
					AssetTag assetTag = getAssetTag(asset.getAssetTag(), nodeName);
					body.setName(assetTag.getSourceTagId());
					// attributes
					com.ge.predix.entity.util.map.Map map = new com.ge.predix.entity.util.map.Map();
					map.put("assetId", asset.getAssetId());
					if (!StringUtils.isEmpty(assetTag.getSourceTagId())) {
						String sourceTagAttribute = assetTag.getSourceTagId().split(":")[1];
						map.put("sourceTagId", sourceTagAttribute);
					}
					body.setAttributes(map);

					// datapoints
					List<Object> datapoint1 = new ArrayList<Object>();
					datapoint1.add(converLocalTimeToUtcTime(data.getTimestamp().getTime()));
					Double convertedValue = getConvertedValue(assetTag.getTagDatasource().getNodeName(),
							Double.parseDouble(data.getValue().toString()));
					datapoint1.add(convertedValue);

					List<Object> datapoints = new ArrayList<Object>();
					datapoints.add(datapoint1);
					body.setDatapoints(datapoints);

					bodies.add(body);
				}
			}
		}

		dpIngestion.setBody(bodies);

		return dpIngestion;
	}

	/**
	 * @param nodeName
	 *            -
	 * @param value
	 *            -
	 * @return -
	 */
	@SuppressWarnings("nls")
	public Double getConvertedValue(String nodeName, Double value) {
		Double convValue = null;
		switch (nodeName.toLowerCase()) {
		case Constants.COMPRESSION_RATIO:
			convValue = value * 9.0 / 65535.0 + 1;
			break;
		case Constants.DISCHG_PRESSURE:
			convValue = value * 100.0 / 65535.0;
			break;
		case Constants.SUCT_PRESSURE:
			convValue = value * 100.0 / 65535.0;
			break;
		case Constants.MAX_PRESSURE:
			convValue = value * 100.0 / 65535.0;
			break;
		case Constants.MIN_PRESSURE:
			convValue = value * 100.0 / 65535.0;
			break;
		case Constants.VELOCITY:
			convValue = value * 0.5 / 65535.0;
			break;
		case Constants.TEMPERATURE:
			convValue = value * 200.0 / 65535.0;
			break;
		default:
			throw new UnsupportedOperationException("nameName=" + nodeName + " not found");
		}
		return convValue;
	}

	private long converLocalTimeToUtcTime(long timeSinceLocalEpoch) {
		return timeSinceLocalEpoch + getLocalToUtcDelta();
	}

	private long getLocalToUtcDelta() {
		Calendar local = Calendar.getInstance();
		local.clear();
		local.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
		return local.getTimeInMillis();
	}

	@SuppressWarnings("nls")
	private AssetTag getAssetTag(LinkedHashMap<String, AssetTag> tags, String nodeName) {
		AssetTag ret = null;
		if (tags != null) {
			for (Entry<String, AssetTag> entry : tags.entrySet()) {
				AssetTag assetTag = entry.getValue();
				// TagDatasource dataSource = assetTag.getTagDatasource();
				if (assetTag != null && !assetTag.getSourceTagId().isEmpty() && nodeName != null
						&& nodeName.toLowerCase().contains(assetTag.getSourceTagId().toLowerCase())) {
					ret = assetTag;
					return ret;
				}
			}
		} else {
			log.warn("2. asset has no assetTags with matching nodeName" + nodeName);
		}
		return ret;
	}
}
