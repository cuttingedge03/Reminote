package com.nadeem.todos.utils;

public class Todo {

	private int id;
	
	private String text;
	private String title;
	private String time;
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return title;
	}

	public void setText(String title,String text) {
		this.text = text;
				this.title=title;
	}
	public String getText1() {
		return text;
	}
	
}
