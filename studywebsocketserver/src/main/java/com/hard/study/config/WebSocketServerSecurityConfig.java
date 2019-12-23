package com.hard.study.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.vote.ScopeVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSocketServerSecurityConfig<S extends Session> extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;
	
	@Autowired
	private FindByIndexNameSessionRepository<S> sessionRepository;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.csrf()
				.ignoringAntMatchers("/ws-stomp/**", "/topic/**", "/app/**", "/message/**")
					.and()
			.headers()
				.frameOptions()
					.sameOrigin()
						.and()
			.authorizeRequests()
				.and()
			.cors();
		
		http
			.authorizeRequests()
				.anyRequest()
					.authenticated();
					/*
				.antMatchers("/ws-stomp/**", "/topic/**", "/app/**", "/message/**")
					.authenticated();*/
		
		http
			.sessionManagement()
				.maximumSessions(10)
					.sessionRegistry(sessionRegistry());
		
		http
			.addFilterAfter(new RequestContextFilter(), BasicAuthenticationFilter.class);
		
	}
	
	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(jwtTokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		
		return defaultTokenServices;
		
	}
	
	@Bean
	public JwtTokenStore jwtTokenStore() {
		
		return new JwtTokenStore(accessTokenConverter);
		
	}
	
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {

		byte[] bdata = null;
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		ClassPathResource cpr = new ClassPathResource("/publickey/publicKey.txt");
		
		try {
			
			bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
			
		} catch(IOException e) {
			
			e.printStackTrace();
			
		}
		
		String publicKey = new String(bdata, StandardCharsets.UTF_8);
		converter.setVerifierKey(publicKey);
		
		return converter;
		
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManager() throws Exception {
		
		AuthenticationManager authenticationManager = super.authenticationManager();
		
		return authenticationManager;
		
	}
	
	@Bean
	public AccessDecisionManager accessDecisionManager() {

		List<AccessDecisionVoter<? extends Object>> accessDecisionVoter = new ArrayList<>();
		accessDecisionVoter.add(new AuthenticatedVoter()); // 인증된 사용자인지
		accessDecisionVoter.add(new ScopeVoter()); // read, write 권한 있는지
		
		return new UnanimousBased(accessDecisionVoter);
		
	}
	
	@Bean
	public FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource() {
		
		return new WebSocketFilterSecurityMetaSource();
		
	}
	
	@Bean
	public FilterSecurityInterceptor filterSecurityInterceptor() throws Exception {
		
		FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
		
		filterSecurityInterceptor.setAuthenticationManager(authenticationManager());
		filterSecurityInterceptor.setSecurityMetadataSource(filterInvocationSecurityMetadataSource());
		filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
		filterSecurityInterceptor.setApplicationEventPublisher(getApplicationContext());
		
		return filterSecurityInterceptor;
		
	}
	
	@Bean
	public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
		
		return new HandlerMappingIntrospector();
		
	}
	
	@Bean
	public SecurityContextChannelInterceptor securityContextChannelInterceptor() {
		
		return new SecurityContextChannelInterceptor();
		
	}
	
	@Bean
	public SpringSessionBackedSessionRegistry<S> sessionRegistry() {
		
		return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
		
	}
	
}
