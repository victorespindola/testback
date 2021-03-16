/**
 * 
 */
package io.pismo.testback.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author victormartins
 *
 */
@Configuration
public class SwaggerConfig {

	
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2).host("localhost")
													  .select()													  
				  									  .apis(RequestHandlerSelectors.basePackage("io.pismo.testback.api"))
				  									  .paths(PathSelectors.regex("/api.*"))
				  									  .build()
				  									  .pathMapping("/");
	}
}
