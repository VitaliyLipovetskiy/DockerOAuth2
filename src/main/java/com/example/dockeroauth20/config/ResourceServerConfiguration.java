package com.example.dockeroauth20.config;

import com.example.dockeroauth20.utils.UUIDGenerator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    public static final String RESOURCE_ID = "oauth-rest-api";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/users/signup").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/users/verify").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/users/password").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/forgot").permitAll()
                .antMatchers(HttpMethod.POST, "/oauth/token").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/oauth/token").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/carriers").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/service_types").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/animal_types").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/cities/**").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/carriers/**").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/pets/qr/**").permitAll()
                .antMatchers("/swagger-ui/**", "/swagger-resources/**", "/webjars/**", "/v2/api-docs").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public FilterRegistrationBean<?> customCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("https://bootcamp-team5.web.app");
        config.addAllowedOrigin("https://elk-kotopes.web.app");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://localhost:3001");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<?> bean = new FilterRegistrationBean<>(new CorsFilter(source));

        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public UUIDGenerator uuidGenerator() {
        return new UUIDGenerator();
    }

//    @Bean
//    public DateProvider dateProvider() {
//        return new DateProvider();
//    }
}

