package com.storesmanagementsystem.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storesmanagementsystem.gateway.contracts.UserInfoBean;
import com.storesmanagementsystem.gateway.domain.UserDetails;
import com.storesmanagementsystem.gateway.repo.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    UserRepository userRepo;

    @Autowired
    ObjectMapper objectMapper;


//
//	@PersistenceUnit
//	private EntityManagerFactory fact;
//
//	@Override
//	public UserInfoBean getUserByUserUd(String userId) {
//		EntityManager mgr = fact.createEntityManager();
//		try {
//			String sqlString = "select * from user_info  where userId=" + userId;
//			Query query = mgr.createNativeQuery(sqlString, UserInfoBean.class);
////			query.setParameter("userId", userId);
//			Object bean = query.getSingleResult();
//			if (null != bean) {
//				UserInfoBean userBean = (UserInfoBean) bean;
////				UserInfoBean userBean = new ModelMapper().map(bean, UserInfoBean.class);
//
//				return userBean;
//			} else {
//				return null;
//			}
//		} catch (Exception e) {
//			for (StackTraceElement ele : e.getStackTrace()) {
//				return null;
//			}
//		}
//		return null;
//	}

    @Override
    public UserInfoBean getUserByUserId(Integer userId) {
        Optional<UserDetails> userInfo = userRepo.findById(userId);
        if (userInfo.isPresent()) {
            UserDetails user = userInfo.get();
            return objectMapper.convertValue(user, UserInfoBean.class);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public UserInfoBean getUser(String username) {
        UserDetails userInfo = userRepo.findByUsername(username);
        if (null != userInfo) {
            UserInfoBean userBean = new UserInfoBean();
            BeanUtils.copyProperties(userInfo, userBean);
            return userBean;
        } else {
            return null;
        }
    }

}
