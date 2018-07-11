package com.appinlet.payhost.Model;

import android.os.Bundle;

import lombok.Builder;

@Builder
public class Card{
	private String exp;
	private String cardNumber;

	public void setExp(String exp){
		this.exp = exp;
	}

	public String getExp(){
		return exp;
	}

	public void setCardNumber(String cardNumber){
		this.cardNumber = cardNumber;
	}

	public String getCardNumber(){
		return cardNumber;
	}


}
