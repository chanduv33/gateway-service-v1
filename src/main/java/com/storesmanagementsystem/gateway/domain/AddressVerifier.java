package com.storesmanagementsystem.gateway.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pincodes")
@Data
@NoArgsConstructor
public class AddressVerifier implements Serializable {

    @Id
    @GeneratedValue
    @Column
    private Integer id;

    @Column
    private Integer pincode;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String country;

}
