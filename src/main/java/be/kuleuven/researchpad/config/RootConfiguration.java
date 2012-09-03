package be.kuleuven.researchpad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import be.kuleuven.researchpad.config.root.ComponentScanNoOpWebConfig;
import be.kuleuven.researchpad.controller.ComponentScanNoOpController;
import be.kuleuven.researchpad.dao.ComponentScanNoOpDao;
import be.kuleuven.researchpad.service.ComponentScanNoOpServices;

@Configuration
@ComponentScan(basePackageClasses = { ComponentScanNoOpWebConfig.class,
		ComponentScanNoOpServices.class, ComponentScanNoOpDao.class, ComponentScanNoOpController.class })
public class RootConfiguration {

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
		Resource[] resources = new ClassPathResource[] { new ClassPathResource(
				"researchpad.properties") };
		pspc.setLocations(resources);
		pspc.setIgnoreUnresolvablePlaceholders(true);
		return pspc;
	}

}
