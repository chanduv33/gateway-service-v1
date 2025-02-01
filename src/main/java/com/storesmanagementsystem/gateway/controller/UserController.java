package com.storesmanagementsystem.gateway.controller;


import com.storesmanagementsystem.gateway.contracts.CommonResponse;
import com.storesmanagementsystem.gateway.contracts.UserInfoBean;
import com.storesmanagementsystem.gateway.contracts.UserLoginInfo;
import com.storesmanagementsystem.gateway.security.UserDetailsService;
import com.storesmanagementsystem.gateway.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;

@RestController
public class UserController {

    private UserDetailsService userDetails;

    private Environment environment;

    @Autowired
    private UserService service;

    @Autowired
    private PasswordEncoder encoder;

    public UserController(UserDetailsService userDetails, Environment environment) {
        this.userDetails = userDetails;
        this.environment = environment;
    }

    @PostMapping(path = "/login")
    public Mono<ResponseEntity<CommonResponse>> login(@RequestBody UserLoginInfo userInfoBean) {

        CommonResponse resp = new CommonResponse();
        Mono<ResponseEntity<CommonResponse>> mono = userDetails.findByUsername(userInfoBean.getUsername())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new IllegalArgumentException("User Not Found")))).map(u -> {
                    System.out.println("Came pass " + userInfoBean.getPassword() + "Existing " + u.getPassword());
                    if (encoder.matches(userInfoBean.getPassword(), u.getPassword())) {
                        String username = u.getUsername();
                        UserInfoBean user = service.getUser(userInfoBean.getUsername());
                        String token = getToken(user);
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("token", token);
                        headers.add("userId", String.valueOf(user.getId()));
                        headers.add("role", user.getRole());

                        resp.setStatus("SUCCESS");
                        return ResponseEntity.ok().headers(headers).body(resp);
                    } else {
                        resp.setStatus("FAILED");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
                    }
                });
        return mono;
    }

    private String getToken(UserInfoBean user) {
    	String expire = environment.getProperty("token.expire");
    	return Jwts.builder().setSubject(String.valueOf(user.getId()))
        .setExpiration(
                new Date(System.currentTimeMillis() + Long.parseLong(expire)))
        .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret")).compact();
    	
    }

}
