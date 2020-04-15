package com.reactiveworks.practice.repo;

import java.util.ArrayList;
import java.util.List;

import com.reactiveworks.practice.model.Student;

public class StudentRepository {
	 List<Student> students;
	
	 public StudentRepository() {
		 students=new ArrayList<Student>();
		 
		    Student student1=new Student();
		    student1.setName("ratna");
		    student1.setPercentage(67.8);
		    student1.setrNo(1);
		    
		    Student student2=new Student();
		    student2.setName("rajani");
		    student2.setPercentage(67.8);
		    student2.setrNo(2);
		    
		    students.add(student2);
		    students.add(student1);
	 }
	
	public List<Student> getStudents(){
	
		    return students;
	}
	
	public Student getStudent(int rollNo) {
		for(int index=0;index<students.size();index++) {
			if(students.get(index).getrNo()==rollNo) {
				return students.get(index);
			}
		}
		return null;
	}

	public Student createStudent(Student student) {
		students.add(student);
		return student;
	}
	
	
}
