/*
package com.dinenowinc.testing;

import com.dinenowinc.dinenow.Constants;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTestNg;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerFactory;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class ContainerPerClassTest extends JerseyTestNg.ContainerPerClassTest {

  protected URI getBaseUri() {
    return UriBuilder.fromUri(Constants.ROOT_API_URL).port(Constants.API_PORT).build(new Object[0]);
  }

  private static ClientConfig clientConfig = new ClientConfig();

  @Override
  protected Application configure() {
    enable(TestProperties.LOG_TRAFFIC);
    enable(TestProperties.DUMP_ENTITY);
    return new ResourceConfig();
  }

  @Override
  protected TestContainerFactory getTestContainerFactory() {
    final URI baseURI = getBaseUri();

    return new TestContainerFactory() {
      @Override
      public TestContainer create(URI uri, DeploymentContext deploymentContext) {
        return new TestContainer() {

          @Override
          public ClientConfig getClientConfig() {
            return clientConfig;
          }

          @Override
          public URI getBaseUri() {
            return baseURI;
          }

          @Override
          public void start() {
            // noop
          }

          @Override
          public void stop() {
            // noop
          }
        };
      }
    };

  }
}
*/
