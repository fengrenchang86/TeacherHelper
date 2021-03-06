package com.frc.teacherhelper.entity;
// Generated 2016-8-4 23:17:06 by Hibernate Tools 3.5.0.Final

/**
 * Role generated by hbm2java
 */
public class Role implements java.io.Serializable {

	private Integer id;
	private String name;
	private String access;
	private Integer type;

	public Role() {
	}

	public Role(String name, String access, Integer type) {
		this.name = name;
		this.access = access;
		this.type = type;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccess() {
		return this.access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
