package com.genpact.bi.batch.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the POJO database table.
 * 
 */
@Entity
@NamedQuery(name="Pojo.findAll", query="SELECT p FROM Pojo p")
public class Pojo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String description;
   @Id
	private Integer id;

	public Pojo() {
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}