package com.storesmanagementsystem.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
public class Address implements Serializable {

    @Id
    @GeneratedValue
    @Column
    private Integer id;

    @Column
    private String addressLine1;

    @Column
    private String addressLine2;

    @Column
    private String street;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String country;

    @Column
    private Integer pincode;

    @Column
    private String addressType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    @ToString.Exclude
    private UserDetails user;
}
