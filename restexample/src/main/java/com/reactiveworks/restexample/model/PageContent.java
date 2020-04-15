package com.reactiveworks.restexample.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PageContent {

	List<Student> students;
	PageLink link;

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public PageLink getLink() {
		return link;
	}

	public void setLink(PageLink link) {
		this.link = link;
	}

}