package kr.co.wayplus.travel.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import kr.co.wayplus.travel.config.handler.UserPageCommonInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import kr.co.wayplus.travel.config.handler.MenuMangeInterceptor;
import kr.co.wayplus.travel.config.handler.TokenAuthorizeInterceptor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableAutoConfiguration
@ComponentScan("kr.co.wayplus.travel")
public class ApplicationConfig implements WebMvcConfigurer {

	@Value("${upload.file.path}")
	String externalFileUploadPath;

	private final TokenAuthorizeInterceptor tokenAuthorizeInterceptor;
	private final UserPageCommonInterceptor userPageCommonInterceptor;
	private final MenuMangeInterceptor menuInterceptor ;

	public ApplicationConfig(
			TokenAuthorizeInterceptor tokenAuthorizeInterceptor,
			UserPageCommonInterceptor userPageCommonInterceptor, MenuMangeInterceptor menuInterceptor) {
		this.tokenAuthorizeInterceptor = tokenAuthorizeInterceptor;
		this.userPageCommonInterceptor = userPageCommonInterceptor;
		this.menuInterceptor = menuInterceptor;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry){
		String ACTIVE_PROFILE = System.getProperty("spring.profiles.active");
		if(ACTIVE_PROFILE != null && ACTIVE_PROFILE.equals("dev")){
			registry.addResourceHandler("/upload/**")
					.setCacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
					.addResourceLocations("file:///"+ externalFileUploadPath);
			registry.addResourceHandler("/images/**")
					.addResourceLocations("classpath:/static/images/")
					.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
			registry.addResourceHandler("/js/**")
					.addResourceLocations("classpath:/static/js/")
					.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
					.resourceChain(true);
			registry.addResourceHandler("/plugin/**")
					.addResourceLocations("classpath:/static/plugin/")
					.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
					.resourceChain(true);
			registry.addResourceHandler("/css/**")
					.addResourceLocations("classpath:/static/css/")
					.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
					.resourceChain(true);
		}else{
			registry.addResourceHandler("/upload/**")
					.setCacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
					.addResourceLocations("file:///"+ externalFileUploadPath);
			registry.addResourceHandler("/images/**")
					.addResourceLocations("classpath:/static/images/")
					.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
			registry.addResourceHandler("/js/**")
					.addResourceLocations("classpath:/static/js/")
					.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
					.resourceChain(true)
					.addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
			registry.addResourceHandler("/plugin/**")
					.addResourceLocations("classpath:/static/plugin/")
					.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
					.resourceChain(true)
					.addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
			registry.addResourceHandler("/css/**")
					.addResourceLocations("classpath:/static/css/")
					.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
					.resourceChain(true)
					.addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
		}
	}

	@Bean
	public ResourceUrlEncodingFilter resourceUrlEncodingFilter(){
		return new ResourceUrlEncodingFilter();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry){
		List<String> excludeUrl = new ArrayList<>();
		excludeUrl.add("/plugin/**");
		excludeUrl.add("/images/**");
		excludeUrl.add("/css/**");
		excludeUrl.add("/font/**");
		excludeUrl.add("/js/**");
		excludeUrl.add("/upload/**");
		excludeUrl.add("/error");
		excludeUrl.add("/error/**");
		excludeUrl.add("/favicon.ico");
		excludeUrl.add("/sitemap.xml");
		excludeUrl.add("/robots.txt");

		registry.addInterceptor(tokenAuthorizeInterceptor)
				.excludePathPatterns(excludeUrl)
				.addPathPatterns("/**");

		excludeUrl.add("/manage/**");
		excludeUrl.add("/admin/**");
		registry.addInterceptor(userPageCommonInterceptor)
				.excludePathPatterns(excludeUrl)
				.addPathPatterns("/**");

		registry.addInterceptor(menuInterceptor)
				.addPathPatterns( "/manage/**","/admin/**" );

	}

	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
		AtomicReference<TomcatServletWebServerFactory> factory = new AtomicReference<>(new TomcatServletWebServerFactory());
		factory.get().addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "|{}"));
		return factory.get();
	}

	@Bean
	public MappingJackson2JsonView jsonView(){
		MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
		mappingJackson2JsonView.setContentType("application/json;charset=utf-8");
		mappingJackson2JsonView.setPrettyPrint(true);
		return mappingJackson2JsonView;
	}

}
