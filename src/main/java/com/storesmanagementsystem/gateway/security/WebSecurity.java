package com.storesmanagementsystem.gateway.security;

import com.storesmanagementsystem.gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
//@EnableWebSecurity
@EnableWebFluxSecurity
public class WebSecurity {

    @Autowired
    Environment environment;

    @Autowired
    UserService service;

    @Autowired
    UserDetailsService userDetails;

    @Autowired
    public PasswordEncoder passwordEncoder;

//    @Autowired
//    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, AuthConverter authConverter, AuthManager authManager) throws Exception {

        CorsConfiguration cors_config = new CorsConfiguration(); //Setting cors config
        CorsConfiguration cors = new CorsConfiguration();
        cors.setExposedHeaders(Arrays.asList("token", "userId", "role"));
        cors.setAllowedOrigins(Arrays.asList("*"));
        cors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(Arrays.asList("*"));
        CookieServerCsrfTokenRepository cookieServerCsrfTokenRepository = CookieServerCsrfTokenRepository.withHttpOnlyFalse();
        http.cors().configurationSource(source -> cors);
        AuthenticationWebFilter filter = new AuthenticationWebFilter(authManager);
        filter.setServerAuthenticationConverter(authConverter);
        http.httpBasic().disable();
        http.formLogin().disable();
        return http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/actuator/**", "/login", "/logout", "/user-service/User/register").permitAll()
                .pathMatchers("/order-service/**").authenticated()
                .pathMatchers("/cart-service/**").authenticated()
                .pathMatchers("/user-service/**").authenticated()
                .pathMatchers("/product-service/**").authenticated()
                //.pathMatchers("/PRODUCT-SERVICE/**").authenticated()
//                .and().exceptionHandling().authenticationEntryPoint(restAuthEntryPoint)
                .and().addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)
                .logout()
                .and()
                .build();
    }

    //    @Bean
    private UserDetailsRepositoryReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager manager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetails);
        return manager;
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////		http.csrf().disable();
////		http.headers().frameOptions().disable();
////		http.headers().disable().cors();
////		http.authorizeRequests().antMatchers("/login").permitAll()
////		http.cors();
//        http.cors().configurationSource(request -> {
//            CorsConfiguration cors = new CorsConfiguration();
//            cors.setExposedHeaders(Arrays.asList("token", "userId", "role"));
//            cors.setAllowedOrigins(Arrays.asList("*"));
//            cors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//            cors.setAllowedHeaders(Arrays.asList("*"));
//            return cors;
//        });
////        http
////                .csrf().disable()
////                .authorizeRequests()
////                .anyRequest().permitAll();
////		CookieServerCsrfTokenRepository cookieServerCsrfTokenRepository = CookieServerCsrfTokenRepository.withHttpOnlyFalse();
////		http.csrf().disable();
////		http.csrf()
////				.csrfTokenRepository(cookieServerCsrfTokenRepository);
//
//		http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and().authorizeRequests()
//				.antMatchers("/security-service/User/register","/actuator/health").permitAll().and().authorizeRequests()
//				.antMatchers("/manufacturer-service/Product", "/manufacturer-service/Product/Cost",
//						"/manufacturer-service/Orders/Payments", "/security-service/Order/deliveredOn",
//						"/manufacturer-service/Product", "/security-service/Order/changeStatus",
//						"/manufacturer-service/Products")
//				.hasRole("MANUFACTURER").and().authorizeRequests()
//				.antMatchers("/admin-service/User", "/admin-service/Users", "/admin-service/User").hasRole("ADMIN")
//				.and().authorizeRequests()
//				.antMatchers("/dealer-service/Product/Price", "/dealer-service/Products", "/dealer-service/Product",
//						"/dealer-service/Product/Price/Min", "/dealer-service/Products/getMansProds",
//						"/Orders/deliveredOn")
//				.hasRole("DEALER").and().authorizeRequests()
//				.antMatchers("/customer-service/Customer/buyProduct", "/customer-service/Products").hasRole("CUSTOMER")
//				.and().authorizeRequests()
//				.antMatchers("/security-service/Orders", "/security-service/Order", "/security-service/Cart/addtocart",
//						"/security-service/Cart/getItems", "/security-service/Cart/remItem")
//				.hasAnyRole("CUSTOMER", "DEALER").anyRequest().authenticated().and()
//				.addFilterBefore(getUsernamePasswordAuthenticationFilter(), CustomUserPasswordAuthFilter.class)
//				.addFilter(new AuthorizationFilter(authenticationManager(), environment, service));
//
//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//    }
}
