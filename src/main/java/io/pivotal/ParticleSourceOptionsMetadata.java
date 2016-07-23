package io.pivotal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("particle")
public class ParticleSourceOptionsMetadata {

	/**
	 * Particle Cloud API URL
	 */
	// "https://api.spark.io/v1/devices/";
	private String baseUrl = "https://api.spark.io/v1/devices/";

	/**
	 * deviceId sending events - all devices if not specified
	 */
	private String deviceId;
	
	/**
	 * accessToken for the account that owns the device
	 */
	private String accessToken;

	/**
	 * eventName or regex 
	 */
	private String eventName = ".*"; // default to all events
	
	
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	// regex
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	@Override
	public String toString() {
		return "ParticleSourceOptionsMetadata [baseUrl=" + baseUrl + ", deviceId=" + deviceId + ", accessToken="
				+ accessToken + ", eventName=" + eventName + "]";
	}
	

}
