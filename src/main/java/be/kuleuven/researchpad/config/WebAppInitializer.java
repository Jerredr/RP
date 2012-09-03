package be.kuleuven.researchpad.config;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer  {

	public void onStartup(ServletContext sc) throws ServletException {
		// Create the 'root' Spring application context
		AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
		root.register(RootConfiguration.class);
		
		// Secures the application
		/*sc.addFilter("securityFilter",
						new DelegatingFilterProxy("springSecurityFilterChain")).addMappingForUrlPatterns(null, false, "/*");
		*/
		// Creates the Spring Container shared by all Servlets and Filters 
		sc.addListener(new ContextLoaderListener(root));	
		
		
		// Secures the application
		sc.addFilter("securityFilter",
								new DelegatingFilterProxy("springSecurityFilterChain")).addMappingForUrlPatterns(null, false, "/*");
				
		// Handles requests into the application
		ServletRegistration.Dynamic appServlet = sc.addServlet("appServlet", 
										new DispatcherServlet(new GenericWebApplicationContext()));
		appServlet.setLoadOnStartup(1);
		
		Set<String> mappingConflicts = appServlet.addMapping("/");
		if (!mappingConflicts.isEmpty()) {
			throw new IllegalStateException("'appServlet' could not be mapped to '/' due " +
											"to an existing mapping. This is a known issue under Tomcat versions " +
											"<= 7.0.14; see https://issues.apache.org/bugzilla/show_bug.cgi?id=51278");
		}
	}

}
