package com.genpact.bi.batch.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the POJO1 database table.
 * 
 */
@Entity
@NamedQuery(name="Pojo1.findAll", query="SELECT p FROM Pojo1 p")
public class Pojo1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String description;

	public Pojo1() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}