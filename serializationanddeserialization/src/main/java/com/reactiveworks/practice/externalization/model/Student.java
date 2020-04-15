package com.reactiveworks.practice.externalization.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Student implements Externalizable {
	private String name;
	private int rNo;
	private double percentage;

	public Student() {
         System.out.println("student object is created");
	}

	public Student( String name,int rNo,double percentage)
	{
      this.name=name;
      this.percentage=percentage;
      this.rNo=rNo;
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

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(name);
		out.writeInt(rNo);
		//out.writeDouble(percentage);
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
          name=(String) in.readObject();
          rNo=in.readInt();
        //  percentage=in.readDouble();
	}

}
