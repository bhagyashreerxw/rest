package com.reactiveworks.practice.model;

import java.io.Serializable;

public class Student1 implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public static String name;
	private int rNo;
	private double percentage;


	public int getrNo() {
		return rNo;
	}

	public void setrNo(int rNo) {
		this.rNo = rNo;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", rNo=" + rNo + ", percentage=" + percentage + "]";
	}


}
