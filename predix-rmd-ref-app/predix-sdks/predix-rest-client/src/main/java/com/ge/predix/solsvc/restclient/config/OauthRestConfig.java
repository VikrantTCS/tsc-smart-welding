package com.ge.predix.solsvc.restclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @author predix
 */
@Component()
@SuppressWarnings({})
public class OauthRestConfig implements IOauthRestConfig {
	@Value("${predix.oauth.resourceProtocol:https}")
	private String oauthResourceProtocol;
	@Value("${predix.oauth.resource:/oauth/token}")
	private String oauthResource;
	@Value("${predix.oauth.restHost:#{null}}")
	private String oauthRestHost;
	@Value("${predix.oauth.restPort:80}")
	private String oauthRestPort;
	@Value("${predix.oauth.grantType:client_credentials}")
	private String oauthGrantType;
	@Value("${predix.oauth.proxyHost:#{null}}")
	private String oauthProxyHost;
	@Value("${predix.oauth.proxyPort:#{null}}")
	private String oauthProxyPort;
	@Value("${predix.oauth.clientId:#{null}}")
	private String oauthClientId;
	@Value("${predix.oauth.clientIdEncode:true}")
	private boolean oauthClientIdEncode;
	@Value("${predix.oauth.tokenType:JWT}")
	private String oauthTokenType;

	@Value("${predix.oauth.userName:#{null}}")
	private String oauthUserName;
	@Value("${predix.oauth.userPassword:#{null}}")
	private String oauthUserPassword;
	@Value("${predix.oauth.encodePassword:true}")
	private boolean oauthEncodeUserPassword;
	@Value("${predix.oauth.certLocation:file:./certs/authTruststore.jks}")
	private String oauthCertLocation;
	@Value("${predix.oauth.certPassword:#{null}}")
	private String oauthCertPassword;
	@Value("${predix.oauth.connectionTimeout:10000}")
	private int oauthConnectionTimeout; //timeout till connection with server is established
	@Value("${predix.oauth.socketTimeout:10000}")
	private int oauthSocketTimeout; //timeout to receive the data from the server
	
	//connection pool parameters
	@Value("${predix.rest.poolMaxSize:10}")
	private int poolMaxSize;
	@Value("${predix.rest.poolValidateAfterInactivityTime:10000}")
	private int poolValidateAfterInactivityTime;
	@Value("${predix.rest.poolConnectionRequestTimeout:10000}")
	private int poolConnectionRequestTimeout;
	@Value("${predix.rest.defaultConnectionTimeout:20000}")
	private int defaultConnectionTimeout;
	@Value("${predix.rest.defaultSocketTimeout:25000}")
	private int defaultSocketTimeout;

	
	/**
	 *  -
	 */
	public OauthRestConfig() {
		super();
	}

	/**
	 * @return -
	 */
	@Override
	public String getOauthResource() {
		return this.oauthResource;
	}

	/**
	 * @return -
	 */
	@Override
	public String getOauthRestHost() {
		return this.oauthRestHost;
	}

	/**
	 * @return -
	 */
	@Override
	public String getOauthRestPort() {
		return this.oauthRestPort;
	}

	/**
	 * @return -
	 */
	@Override
	public String getOauthGrantType() {
		return this.oauthGrantType;
	}

	/**
	 * @return -
	 */
	@Override
	public String getOauthCertLocation() {
		return this.oauthCertLocation;
	}

	/**
	 * @return -
	 */
	@Override
	public String getOauthCertPassword() {
		return this.oauthCertPassword;
	}

	/**
	 * @return -
	 */
	@Override
	public String getOauthClientId() {
		return this.oauthClientId;
	}

	/**
	 * @return -
	 */
	@Override
	public boolean getOauthClientIdEncode() {
		return this.oauthClientIdEncode;
	}

	/**
	 * @return -
	 */
	@Override
	public String getOauthProxyHost() {
		return this.oauthProxyHost;
	}

	/**
	 * @return -
	 */
	@Override
	public String getOauthProxyPort() {
		return this.oauthProxyPort;
	}

	/**
	 * @return -
	 */
	@Override
	public String getOauthTokenType() {
		return this.oauthTokenType;
	}

	/**
	 * @return the userName
	 */
	@Override
	public String getOauthUserName() {
		return this.oauthUserName;
	}

	/**
	 * @return the password
	 */
	@Override
	public String getOauthUserPassword() {
		return this.oauthUserPassword;
	}

	/**
	 * @return the encodePassword
	 */
	@Override
	public boolean isOauthEncodeUserPassword() {
		return this.oauthEncodeUserPassword;
	}

	@Override
	public int getOauthSocketTimeout() {
		return this.oauthSocketTimeout;
	}

	@Override
	public int getOauthConnectionTimeout() {
		return this.oauthConnectionTimeout;
	}

	/**
	 * @return the oauthResourceProtocol
	 */
	@Override
	public String getOauthResourceProtocol() {
		return this.oauthResourceProtocol;
	}

	
	/* (non-Javadoc)
	 * @see com.ge.predix.solsvc.restclient.IRestConfig#printName()
	 */
	@Override
	public String printName() {
		return "RestConfig"; //$NON-NLS-1$
	}	
	
	
	@Override
	public int getPoolMaxSize() {
		return this.poolMaxSize;
	}

	/* (non-Javadoc)
	 * @see com.ge.predix.solsvc.restclient.config.IOauthRestConfig#getValidateAfterInactivityTime()
	 */
	@Override
	public int getPoolValidateAfterInactivityTime() {
		return this.poolValidateAfterInactivityTime;
	}

	/* (non-Javadoc)
	 * @see com.ge.predix.solsvc.restclient.config.IOauthRestConfig#getOauthConnectionRequestTimeout()
	 */
	@Override
	public int getPoolConnectionRequestTimeout() {
		return this.poolConnectionRequestTimeout;
	}

	/* (non-Javadoc)
	 * @see com.ge.predix.solsvc.restclient.config.IOauthRestConfig#getDefaultConnectionTimeout()
	 */
	@Override
	public int getDefaultConnectionTimeout() {
		return this.defaultConnectionTimeout;
	}

	/* (non-Javadoc)
	 * @see com.ge.predix.solsvc.restclient.config.IOauthRestConfig#getDefaultSocketTimeout()
	 */
	@Override
	public int getDefaultSocketTimeout() {
		return this.defaultSocketTimeout;
	}

}
