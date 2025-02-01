package com.storesmanagementsystem.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString.Exclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "user_info")
public class UserDetails implements Serializable {
	
	@Id
	@GeneratedValue
	@Column
	private int id;
	
	@Column
	private String name;
	
	@Column
	private String role;
	
	@Column
	private String username;
	
	@Column
	private String password;
	
	@Column
	private long mobileNumber;

	@Column
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@Exclude
	@JsonIgnore
	private List<Address> addresses;
	
//	@OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
//	@Exclude
//	private List<OrderDetails> orders = new LinkedList<OrderDetails>();
//
//	@OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
//	@Exclude
//	private List<CartDetails> items = new LinkedList<CartDetails>();
//
//	@OneToMany(cascade = CascadeType.ALL,mappedBy = "users")
//	@EqualsAndHashCode.Exclude
//	private List<ProductDetails> products = new ArrayList<ProductDetails>();
//
//	@OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
//	private List<DealerProduct> dealersProds = new ArrayList<DealerProduct>();

}
