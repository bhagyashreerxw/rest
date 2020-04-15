package com.reactiveworks.practice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Student {

	private String name;
	private int rollNo;
	private double percentage;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getrNo() {
		return rollNo;
	}

	public void setrNo(int rNo) {
		this.rollNo = rNo;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", rNo=" + rollNo + ", percentage=" + percentage + "]";
	}

}
