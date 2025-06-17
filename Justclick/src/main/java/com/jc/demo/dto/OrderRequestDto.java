package com.jc.demo.dto;

public class OrderRequestDto {
	
	
    private String paymentType;
    private Integer selectedAddress; // <-- This will store the ID of selected MasterAddress
 private  Integer drop;
 private String trigger;
    public String getPaymentType() {
        return paymentType;
    }
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
    public Integer getSelectedAddress() {
        return selectedAddress;
    }
    public void setSelectedAddress(Integer selectedAddress) {
        this.selectedAddress = selectedAddress;
    }
}

