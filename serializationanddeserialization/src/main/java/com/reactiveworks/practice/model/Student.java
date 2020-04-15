package com.reactiveworks.practice.model;

import java.io.Serializable;

public class Student implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private int rNo;
	private double percentage;
	
	public Student() {
		System.out.println("student object is created");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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
