package com.reactiveworks.iplrestservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PageLink {

	private String next;
	private String prev;

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getPrev() {
		return prev;
	}

	public void setPrev(String prev) {
		this.prev = prev;
	}

	@Override
	public String toString() {
		return "PageLink [next=" + next + ", prev=" + prev + "]";
	}

}
