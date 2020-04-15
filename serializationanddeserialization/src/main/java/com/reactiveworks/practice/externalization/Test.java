package com.reactiveworks.practice.externalization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.reactiveworks.practice.externalization.model.Student;


public class Test {

	public static void main(String[] args) {
	
		Student student=new Student();
		student.setName("ratna");
		student.setPercentage(67.8);
		student.setrNo(1);
		try {
			//Serialization
		  FileOutputStream fout=new FileOutputStream("f.txt");  
		  ObjectOutputStream out=new ObjectOutputStream(fout);  
		  out.writeObject(student);  
		  out.flush();  
		  //closing the stream  
		  out.close();  
		  System.out.println("success");  
		  
		  //deserialization
		  ObjectInputStream in=new ObjectInputStream(new FileInputStream("f.txt"));  
		  Student s=(Student)in.readObject();  
		  System.out.println("deserialized object: "+s);  
		  //closing the stream  
		  in.close();  
		  
		  }catch(Exception e){System.out.println(e);}



	}

}
