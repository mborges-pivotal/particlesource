package io.pivotal;

import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@EnableBinding( Source.class )
public class ParticleSource {

    @Autowired
    @Qualifier("output")
    private MessageChannel _output;

    @Value( "${particle.baseUrl}" ) String _baseUrl;
    @Value( "${particle.deviceId}" ) String _deviceId;
    @Value( "${particle.accessToken}" ) String _accessToken;
    @Value( "${particle.eventName}" ) String _eventName;

    public void handleMessage()
    {
        Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
        WebTarget target = client.target(getEventSourceUrl());

        EventInput eventInput = target.request().get(EventInput.class);
        while (!eventInput.isClosed())
        {
            final InboundEvent inboundEvent = eventInput.read();
            if (inboundEvent == null)
            {
                // connection has been closed
                break;
            }

            String name = inboundEvent.getName();
            if ( name != null && name.equals( _eventName ) )
            {
                String data = inboundEvent.readData(String.class);
                _output.send(MessageBuilder.withPayload( data ).build() );
            }
        }
    }

    private String getEventSourceUrl()
    {
        return _baseUrl + _deviceId + "/events?access_token=" + _accessToken;
    }

    @Bean
    CommandLineRunner startup()
    {
        return args -> handleMessage();
    }
}
