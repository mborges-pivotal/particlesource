package io.pivotal;

import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@EnableBinding(Source.class)
@EnableConfigurationProperties(ParticleSourceOptionsMetadata.class)
public class ParticleSource {

	private static Logger logger = LoggerFactory.getLogger(ParticleSource.class);

	private volatile boolean running = false;

	@Autowired
	@Qualifier("output")
	private MessageChannel _output;

	@Autowired
	private ParticleSourceOptionsMetadata options;

	@PostConstruct
	public void init() {
		logger.info("####### init ##########");
	}

	@PreDestroy
	public void destroy() {
		logger.info("####### destroy ##########");
		running = false;
	}

	public void handleMessage() {

		logger.info("####### handle message ##########");
		logger.info(options.toString());
		
		Pattern r = Pattern.compile(options.getEventName());

		Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
		WebTarget target = client.target(getEventSourceUrl());

		EventInput eventInput = target.request().get(EventInput.class);

		running = true;
		while (!eventInput.isClosed() && running) {
			final InboundEvent inboundEvent = eventInput.read();

			if (inboundEvent == null) {
				logger.info("Connection has been closed");
				break;
			}

			String name = inboundEvent.getName();
			if (name != null && r.matcher(name).matches()) {
				String data = inboundEvent.readData(String.class);
				logger.info("EventName: {} - {}", name, data);
				_output.send(MessageBuilder.withPayload(data).setHeader("eventName", name).build());

			}
		}
		running = false;
	}

	public ParticleSourceOptionsMetadata getOptions() {
		return options;
	}

	@Bean
	CommandLineRunner startup() {
		return args -> handleMessage();
	}

	////////////////////////////////////////////
	// Helper Methods
	////////////////////////////////////////////

	private String getEventSourceUrl() {
		return options.getBaseUrl() + options.getDeviceId() + "/events?access_token=" + options.getAccessToken();
	}

}
