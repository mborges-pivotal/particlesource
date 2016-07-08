package io.pivotal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("particle")
public class ParticleSourceOptionsMetadata {

	private String baseUrl;
	private String deviceId;
	private String accessToken;
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
