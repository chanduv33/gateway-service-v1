package com.storesmanagementsystem.gateway.contracts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true,value = {"addresses"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoBean {

	private Integer id;

	private String name;

	private String role;

	private String username;

	private String password;

	private Long mobileNumber;

}
