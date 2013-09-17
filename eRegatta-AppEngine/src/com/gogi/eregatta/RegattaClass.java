package com.gogi.eregatta;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RegattaClass {
	@Id
	private int id;
	private String regattaid;
	private String regattaclass;
	private String discipline;
	private String grade;			
	private String type;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRegattaId() {
		return regattaid;
	}
	public void setRegattaId(String regattaid) {
		this.regattaid = regattaid;
	}
	public String getRegattaClass() {
		return regattaclass;
	}
	public void setRegattaClass(String regattaclass) {
		this.regattaclass = regattaclass;
	}
	public String getDiscipline() {
		return discipline;
	}
	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
