package com.smart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private String Secondname;
	private String work;
	private String Email;
	private String phone;
	private String image;
	@Column(length = 1000)
	private String description;
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Contact(int id, String name, String secondname, String work, String email, String phone, String image,
			String description) {
		super();
		this.id = id;
		this.name = name;
		Secondname = secondname;
		this.work = work;
		Email = email;
		this.phone = phone;
		this.image = image;
		this.description = description;
	}
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSecondname() {
		return Secondname;
	}
	public void setSecondname(String secondname) {
		Secondname = secondname;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", Secondname=" + Secondname + ", work=" + work + ", Email="
				+ Email + ", phone=" + phone + ", image=" + image + ", description=" + description + "]";
	}

}
