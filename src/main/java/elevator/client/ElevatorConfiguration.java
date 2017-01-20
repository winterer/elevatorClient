package elevator.client;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import sqelevator.IElevator;

@Configuration
@EnableConfigurationProperties(ElevatorProperties.class)
public class ElevatorConfiguration {
	private ElevatorProperties properties;

	public ElevatorConfiguration(ElevatorProperties properties) {
		this.properties = properties;
	}

	/**
	 * {@link RmiProxyFactoryBean} looks up the {@link IElevator} interface and
	 * wraps it into an {@link IElevatorController}.
	 * 
	 * @return a factory bean that creates a remote {@link IElevatorController}
	 *         on demand.
	 */
	@Bean
	public RmiProxyFactoryBean getElevatorController() {
		RmiProxyFactoryBean factoryBean = new RmiProxyFactoryBean();
		factoryBean.setLookupStubOnStartup(false);
		factoryBean.setServiceUrl(properties.getServiceUrl());
		factoryBean.setServiceInterface(IElevatorController.class);
		factoryBean.setRefreshStubOnConnectFailure(true);
		return factoryBean;
	}
}
