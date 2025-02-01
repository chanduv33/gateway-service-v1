package com.storesmanagementsystem.gateway.security;

import com.storesmanagementsystem.gateway.contracts.UserInfoBean;
import com.storesmanagementsystem.gateway.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    UserRepository usersRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
      com.storesmanagementsystem.gateway.domain.UserDetails user = usersRepository.findByUsername(username);
       if(user == null){
           return Mono.error(new IllegalArgumentException("User Not Found"));
       } else {
           UserDetailsImpl userDetails = new UserDetailsImpl();
           UserInfoBean userInfoBean = new UserInfoBean();
           userInfoBean.setId(user.getId());
           userInfoBean.setUsername(user.getUsername());
           userInfoBean.setRole(user.getRole());
           userInfoBean.setPassword(user.getPassword());
           userDetails.setUser(userInfoBean);
           return Mono.just(userDetails);
       }
//        return .switchIfEmpty(Mono.defer(() -> Mono.error(new IllegalArgumentException("User Not Found")))).map(
//                user->{
//                    UserInfoBean userInfoBean = new UserInfoBean();
//                    userInfoBean.setUserId(user.);
//                }
//        );
    }
}
