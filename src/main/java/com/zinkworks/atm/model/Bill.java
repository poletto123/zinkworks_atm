package com.zinkworks.atm.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Bill {

    @Id
    @ApiModelProperty(value = "type of bill (50, 20, 10, 5)", example = "50")
    private int billType;

    @ApiModelProperty(value = "bill quantity", example = "30")
    private int quantity;

    public Bill(int billType, int quantity) {
        this.billType = billType;
        this.quantity = quantity;
    }

    public Bill() {
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(int billType) {
        this.billType = billType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
