package com.kalaerik.productmng.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @NotEmpty(message="Code must not be empty")
    @Size(min = 10, max = 10, message = "The length of code must be exactly 10 characters.")
    private String code;
    @NotEmpty(message = "Name field must not be empty.")
    private String name;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be 0 (zero) or grater number")
    private double price_hrk;
    @Transient
    @JsonInclude
    private double price_eur;
    private String description;
    @NotNull
    @Pattern(regexp = "^true$|^false$", message = "Allowed input for is_available: true or false")
    private String is_available;



    public Boolean getIs_available() {
        return Boolean.valueOf(is_available);
    }
}
