package it.polito.tdp.ufo.model;

public class YearAndSightings {

	private Integer year;
	private Integer sightings;
	
	public YearAndSightings(Integer year, Integer sightings) {
		super();
		this.year = year;
		this.sightings = sightings;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getSightings() {
		return sightings;
	}

	public void setSightings(Integer sightings) {
		this.sightings = sightings;
	}

	@Override
	public String toString() {
		return year + " - " + sightings + " avvistamenti";
	}
	
	
}
