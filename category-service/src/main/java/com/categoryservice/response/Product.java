package com.categoryservice.response;

import java.util.List;

public class Product {
	private String id;
	private String name;
	private String brand;
	private String description;
	private Price price;
	private Category category;
	private Inventory inventory;
	private List<Attributes> attributes;

	
	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Product(String id, String name, String brand, String description, Price price, Category category,
			Inventory inventory, List<Attributes> attributes) {
		super();
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.description = description;
		this.price = price;
		this.category = category;
		this.inventory = inventory;
		this.attributes = attributes;
	}

	// getters and setters omitted for brevity

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public List<Attributes> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attributes> attributes) {
		this.attributes = attributes;
	}

}