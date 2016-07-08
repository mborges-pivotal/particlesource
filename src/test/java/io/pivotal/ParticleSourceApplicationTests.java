package io.pivotal;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ParticleSourceApplicationTest
 * 
 * TODO: Look into proper testing
 * 
 * @see http://docs.spring.io/spring-cloud-stream/docs/current-SNAPSHOT/
 *      reference/htmlsingle/#_testing
 * 
 * @author mborges
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ParticleSourceApplication.class)
@ActiveProfiles("test")
public class ParticleSourceApplicationTests {

	private static Logger logger = LoggerFactory.getLogger(ParticleSourceApplicationTests.class);

	@Autowired
	private ParticleSource source;

	@Test
	public void contextLoads() {
		logger.info("############## - " + this.source.getOptions().getDeviceId());
		assertNotNull(this.source.getOptions().getDeviceId());
		source.destroy();
	}

}
