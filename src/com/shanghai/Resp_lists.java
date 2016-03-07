package com.shanghai;

import java.util.List;

public class Resp_lists {
	private java.util.List<String> list_users,dates, provinces, citys ,projects, road_names, 
	directions, start_times, end_times, v_counts ,nv_counts ,ps_counts;

	public Resp_lists(List<String> listUsers, List<String> dates,
			List<String> provinces, List<String> citys, List<String> projects,
			List<String> roadNames, List<String> directions,
			List<String> startTimes, List<String> endTimes,
			List<String> vCounts, List<String> nvCounts, List<String> psCounts) {
		super();
		list_users = listUsers;
		this.dates = dates;
		this.provinces = provinces;
		this.citys = citys;
		this.projects = projects;
		road_names = roadNames;
		this.directions = directions;
		start_times = startTimes;
		end_times = endTimes;
		v_counts = vCounts;
		nv_counts = nvCounts;
		ps_counts = psCounts;
	}

	public java.util.List<String> getList_users() {
		return list_users;
	}

	public void setList_users(java.util.List<String> listUsers) {
		list_users = listUsers;
	}

	public java.util.List<String> getDates() {
		return dates;
	}

	public void setDates(java.util.List<String> dates) {
		this.dates = dates;
	}

	public java.util.List<String> getProvinces() {
		return provinces;
	}

	public void setProvinces(java.util.List<String> provinces) {
		this.provinces = provinces;
	}

	public java.util.List<String> getCitys() {
		return citys;
	}

	public void setCitys(java.util.List<String> citys) {
		this.citys = citys;
	}

	public java.util.List<String> getProjects() {
		return projects;
	}

	public void setProjects(java.util.List<String> projects) {
		this.projects = projects;
	}

	public java.util.List<String> getRoad_names() {
		return road_names;
	}

	public void setRoad_names(java.util.List<String> roadNames) {
		road_names = roadNames;
	}

	public java.util.List<String> getDirections() {
		return directions;
	}

	public void setDirections(java.util.List<String> directions) {
		this.directions = directions;
	}

	public java.util.List<String> getStart_times() {
		return start_times;
	}

	public void setStart_times(java.util.List<String> startTimes) {
		start_times = startTimes;
	}

	public java.util.List<String> getEnd_times() {
		return end_times;
	}

	public void setEnd_times(java.util.List<String> endTimes) {
		end_times = endTimes;
	}

	public java.util.List<String> getV_counts() {
		return v_counts;
	}

	public void setV_counts(java.util.List<String> vCounts) {
		v_counts = vCounts;
	}

	public java.util.List<String> getNv_counts() {
		return nv_counts;
	}

	public void setNv_counts(java.util.List<String> nvCounts) {
		nv_counts = nvCounts;
	}

	public java.util.List<String> getPs_counts() {
		return ps_counts;
	}

	public void setPs_counts(java.util.List<String> psCounts) {
		ps_counts = psCounts;
	}
	
}
