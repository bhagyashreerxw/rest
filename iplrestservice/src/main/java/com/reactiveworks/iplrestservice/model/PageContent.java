package com.reactiveworks.iplrestservice.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PageContent {

	List<Match> matches;
	PageLink link;
	public List<Match> getMatches() {
		return matches;
	}
	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}
	public PageLink getLink() {
		return link;
	}
	public void setLink(PageLink link) {
		this.link = link;
	}

	

}