package com.austindewey.lab.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Index {

	@NotNull @Min(value=1, message="{iterations.min}")
	private Integer iterations;
	@NotNull @Min(value=2, message="{people.min}")
	private Integer people;
	private Double result;
	
	public Index(Integer iterations, Integer people, Double result) {
		this.iterations = iterations;
		this.people = people;
		this.result = result;
	}
	
	public Index() {
		super();
	}
	
	public void setIterations(Integer iterations) {
		this.iterations = iterations;
	}
	
	public Integer getIterations() {
		return iterations;
	}
	
	public void setPeople(Integer people) {
		this.people = people;
	}
	
	public Integer getPeople() {
		return people;
	}
	
	public void setResult(Double result) {
		this.result = result;
	}
	
	public Double getResult() {
		return result;
	}
}
