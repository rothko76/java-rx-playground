package com.rotem.reactordemo.beans;

import org.springframework.beans.factory.annotation.Autowired;

public class BeanA {
	
	@Autowired
	public BeanA(String name) {
		super();
		this.name = name;
	}

	String name;

}
