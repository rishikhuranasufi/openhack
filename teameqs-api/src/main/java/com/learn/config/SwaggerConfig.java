package com.learn.config;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@ConditionalOnClass(Docket.class)
@ConditionalOnMissingBean(Docket.class)
public class SwaggerConfig
{
	@Value("${swagger.api.title}")
	private String apiTitle;

	@Value("${swagger.api.contact.info}")
	private String apiContactInfo;

	@Value("${swagger.api.licence.url}")
	private String licenseUrl;

	@Value("${swagger.terms.of.service.url}")
	private String termsOfServiceUrl;

	@Value("${swagger.api.description}")
	private String apiDescription;

	@Value("${swagger.api.groupName}")
	private String groupName;

	@Value("${swagger.api.version}")
	private String apiVersion;

	@Value("${swagger.api.path.regex:/v.*/.*}")
	private String pathRegex;

	@Value("${swagger.enabled:true}")
	private boolean enabled;

	@Bean
	public Docket microServiceApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.groupName(groupName)
				.apiInfo(apiInfo())
                .enable(enabled)
				.directModelSubstitute(LocalDate.class, java.sql.Date.class)
				.directModelSubstitute(LocalDateTime.class, java.util.Date.class)
				.select()
				.paths(apiPaths())
				.build();
	}

	private Predicate<String> apiPaths() {
		return regex(pathRegex);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title(apiTitle)
				.description(apiDescription)
				.termsOfServiceUrl(termsOfServiceUrl)
				.contact(new Contact("Adarsh Mehrotra and Rishi Khurana", "", ""))
				.license("")
				.licenseUrl(licenseUrl)
				.version(apiVersion)
				.build();
	}

}