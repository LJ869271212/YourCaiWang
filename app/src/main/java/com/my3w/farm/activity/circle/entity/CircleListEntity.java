package com.my3w.farm.activity.circle.entity;

import java.util.ArrayList;

public class CircleListEntity {

	private int id;
	private int uid;
	private String usericon;
	private String username;
	private String content;
	private long comcount;
	private ArrayList<CircleListImageListEntity> imagelist;
	private ArrayList<CircleListCommentListEntity> commentlist;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUsericon() {
		return usericon;
	}

	public void setUsericon(String usericon) {
		this.usericon = usericon;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getComcount() {
		return comcount;
	}

	public void setComcount(long comcount) {
		this.comcount = comcount;
	}

	public ArrayList<CircleListImageListEntity> getImagelist() {
		return imagelist;
	}

	public void setImagelist(ArrayList<CircleListImageListEntity> imagelist) {
		this.imagelist = imagelist;
	}

	public ArrayList<CircleListCommentListEntity> getCommentlist() {
		return commentlist;
	}

	public void setCommentlist(ArrayList<CircleListCommentListEntity> commentlist) {
		this.commentlist = commentlist;
	}

}
