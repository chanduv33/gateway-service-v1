package com.storesmanagementsystem.gateway.contracts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse {
	private UserInfoBean user;
	private String status;
	private List<com.storesmanagementsystem.gateway.contracts.Error> errors;
}
