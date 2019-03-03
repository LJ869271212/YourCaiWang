package com.my3w.farm.activity.campaign.entity;

public class ImageInfoListEntity {

	private String id;
	private String title;
	private String content;
	private String uid;
	private String username;
	private String create;
	private String clickCount;
	private String isRecom;
	private String all_pic;
	private String comcount;
	private String toucount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCreate() {
		return create;
	}

	public void setCreate(String create) {
		this.create = create;
	}

	public String getClickCount() {
		return clickCount;
	}

	public void setClickCount(String clickCount) {
		this.clickCount = clickCount;
	}

	public String getIsRecom() {
		return isRecom;
	}

	public void setIsRecom(String isRecom) {
		this.isRecom = isRecom;
	}

	public String getAll_pic() {
		return all_pic;
	}

	public void setAll_pic(String all_pic) {
		this.all_pic = all_pic;
	}

	public String getComcount() {
		return comcount;
	}

	public void setComcount(String comcount) {
		this.comcount = comcount;
	}

	public String getToucount() {
		return toucount;
	}

	public void setToucount(String toucount) {
		this.toucount = toucount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((all_pic == null) ? 0 : all_pic.hashCode());
		result = prime * result + ((clickCount == null) ? 0 : clickCount.hashCode());
		result = prime * result + ((comcount == null) ? 0 : comcount.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((create == null) ? 0 : create.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isRecom == null) ? 0 : isRecom.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((toucount == null) ? 0 : toucount.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageInfoListEntity other = (ImageInfoListEntity) obj;
		if (all_pic == null) {
			if (other.all_pic != null)
				return false;
		} else if (!all_pic.equals(other.all_pic))
			return false;
		if (clickCount == null) {
			if (other.clickCount != null)
				return false;
		} else if (!clickCount.equals(other.clickCount))
			return false;
		if (comcount == null) {
			if (other.comcount != null)
				return false;
		} else if (!comcount.equals(other.comcount))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (create == null) {
			if (other.create != null)
				return false;
		} else if (!create.equals(other.create))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isRecom == null) {
			if (other.isRecom != null)
				return false;
		} else if (!isRecom.equals(other.isRecom))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (toucount == null) {
			if (other.toucount != null)
				return false;
		} else if (!toucount.equals(other.toucount))
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ImageInfoListEntity [id=" + id + ", title=" + title + ", content=" + content + ", uid=" + uid + ", username=" + username
				+ ", create=" + create + ", clickCount=" + clickCount + ", isRecom=" + isRecom + ", all_pic=" + all_pic + ", comcount="
				+ comcount + ", toucount=" + toucount + "]";
	}

}
