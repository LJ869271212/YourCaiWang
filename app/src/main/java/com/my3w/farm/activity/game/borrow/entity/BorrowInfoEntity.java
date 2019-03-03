package com.my3w.farm.activity.game.borrow.entity;

public class BorrowInfoEntity {

	private String id;
	private String pid;
	private String fromuser;
	private String touser;
	private String address;
	private String cade;
	private String phone;
	private String land_name;
	private String qq;
	private String email;
	private String borrow_name;
	private String return_name;
	private String title;
	private String description;
	private String borrow_num;
	private String br_date;
	private String re_date;
	private String addtime;
	private String bx_where;
	private String thumb_pic;
	private String shstate;
	private String borrow_unit;
	private String borrow_price;
	private String province_id;
	private String city_id;
	private String county_id;
	private String click_num;
	private String isread;
	private String isignord;
	private String restate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getFromuser() {
		return fromuser;
	}

	public void setFromuser(String fromuser) {
		this.fromuser = fromuser;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCade() {
		return cade;
	}

	public void setCade(String cade) {
		this.cade = cade;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLand_name() {
		return land_name;
	}

	public void setLand_name(String land_name) {
		this.land_name = land_name;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBorrow_name() {
		return borrow_name;
	}

	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}

	public String getReturn_name() {
		return return_name;
	}

	public void setReturn_name(String return_name) {
		this.return_name = return_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBorrow_num() {
		return borrow_num;
	}

	public void setBorrow_num(String borrow_num) {
		this.borrow_num = borrow_num;
	}

	public String getBr_date() {
		return br_date;
	}

	public void setBr_date(String br_date) {
		this.br_date = br_date;
	}

	public String getRe_date() {
		return re_date;
	}

	public void setRe_date(String re_date) {
		this.re_date = re_date;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getBx_where() {
		return bx_where;
	}

	public void setBx_where(String bx_where) {
		this.bx_where = bx_where;
	}

	public String getThumb_pic() {
		return thumb_pic;
	}

	public void setThumb_pic(String thumb_pic) {
		this.thumb_pic = thumb_pic;
	}

	public String getShstate() {
		return shstate;
	}

	public void setShstate(String shstate) {
		this.shstate = shstate;
	}

	public String getBorrow_unit() {
		return borrow_unit;
	}

	public void setBorrow_unit(String borrow_unit) {
		this.borrow_unit = borrow_unit;
	}

	public String getBorrow_price() {
		return borrow_price;
	}

	public void setBorrow_price(String borrow_price) {
		this.borrow_price = borrow_price;
	}

	public String getProvince_id() {
		return province_id;
	}

	public void setProvince_id(String province_id) {
		this.province_id = province_id;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getCounty_id() {
		return county_id;
	}

	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}

	public String getClick_num() {
		return click_num;
	}

	public void setClick_num(String click_num) {
		this.click_num = click_num;
	}

	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}

	public String getIsignord() {
		return isignord;
	}

	public void setIsignord(String isignord) {
		this.isignord = isignord;
	}

	public String getRestate() {
		return restate;
	}

	public void setRestate(String restate) {
		this.restate = restate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((addtime == null) ? 0 : addtime.hashCode());
		result = prime * result + ((borrow_name == null) ? 0 : borrow_name.hashCode());
		result = prime * result + ((borrow_num == null) ? 0 : borrow_num.hashCode());
		result = prime * result + ((borrow_price == null) ? 0 : borrow_price.hashCode());
		result = prime * result + ((borrow_unit == null) ? 0 : borrow_unit.hashCode());
		result = prime * result + ((br_date == null) ? 0 : br_date.hashCode());
		result = prime * result + ((bx_where == null) ? 0 : bx_where.hashCode());
		result = prime * result + ((cade == null) ? 0 : cade.hashCode());
		result = prime * result + ((city_id == null) ? 0 : city_id.hashCode());
		result = prime * result + ((click_num == null) ? 0 : click_num.hashCode());
		result = prime * result + ((county_id == null) ? 0 : county_id.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fromuser == null) ? 0 : fromuser.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isignord == null) ? 0 : isignord.hashCode());
		result = prime * result + ((isread == null) ? 0 : isread.hashCode());
		result = prime * result + ((land_name == null) ? 0 : land_name.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
		result = prime * result + ((province_id == null) ? 0 : province_id.hashCode());
		result = prime * result + ((qq == null) ? 0 : qq.hashCode());
		result = prime * result + ((re_date == null) ? 0 : re_date.hashCode());
		result = prime * result + ((restate == null) ? 0 : restate.hashCode());
		result = prime * result + ((return_name == null) ? 0 : return_name.hashCode());
		result = prime * result + ((shstate == null) ? 0 : shstate.hashCode());
		result = prime * result + ((thumb_pic == null) ? 0 : thumb_pic.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((touser == null) ? 0 : touser.hashCode());
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
		BorrowInfoEntity other = (BorrowInfoEntity) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (addtime == null) {
			if (other.addtime != null)
				return false;
		} else if (!addtime.equals(other.addtime))
			return false;
		if (borrow_name == null) {
			if (other.borrow_name != null)
				return false;
		} else if (!borrow_name.equals(other.borrow_name))
			return false;
		if (borrow_num == null) {
			if (other.borrow_num != null)
				return false;
		} else if (!borrow_num.equals(other.borrow_num))
			return false;
		if (borrow_price == null) {
			if (other.borrow_price != null)
				return false;
		} else if (!borrow_price.equals(other.borrow_price))
			return false;
		if (borrow_unit == null) {
			if (other.borrow_unit != null)
				return false;
		} else if (!borrow_unit.equals(other.borrow_unit))
			return false;
		if (br_date == null) {
			if (other.br_date != null)
				return false;
		} else if (!br_date.equals(other.br_date))
			return false;
		if (bx_where == null) {
			if (other.bx_where != null)
				return false;
		} else if (!bx_where.equals(other.bx_where))
			return false;
		if (cade == null) {
			if (other.cade != null)
				return false;
		} else if (!cade.equals(other.cade))
			return false;
		if (city_id == null) {
			if (other.city_id != null)
				return false;
		} else if (!city_id.equals(other.city_id))
			return false;
		if (click_num == null) {
			if (other.click_num != null)
				return false;
		} else if (!click_num.equals(other.click_num))
			return false;
		if (county_id == null) {
			if (other.county_id != null)
				return false;
		} else if (!county_id.equals(other.county_id))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fromuser == null) {
			if (other.fromuser != null)
				return false;
		} else if (!fromuser.equals(other.fromuser))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isignord == null) {
			if (other.isignord != null)
				return false;
		} else if (!isignord.equals(other.isignord))
			return false;
		if (isread == null) {
			if (other.isread != null)
				return false;
		} else if (!isread.equals(other.isread))
			return false;
		if (land_name == null) {
			if (other.land_name != null)
				return false;
		} else if (!land_name.equals(other.land_name))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (pid == null) {
			if (other.pid != null)
				return false;
		} else if (!pid.equals(other.pid))
			return false;
		if (province_id == null) {
			if (other.province_id != null)
				return false;
		} else if (!province_id.equals(other.province_id))
			return false;
		if (qq == null) {
			if (other.qq != null)
				return false;
		} else if (!qq.equals(other.qq))
			return false;
		if (re_date == null) {
			if (other.re_date != null)
				return false;
		} else if (!re_date.equals(other.re_date))
			return false;
		if (restate == null) {
			if (other.restate != null)
				return false;
		} else if (!restate.equals(other.restate))
			return false;
		if (return_name == null) {
			if (other.return_name != null)
				return false;
		} else if (!return_name.equals(other.return_name))
			return false;
		if (shstate == null) {
			if (other.shstate != null)
				return false;
		} else if (!shstate.equals(other.shstate))
			return false;
		if (thumb_pic == null) {
			if (other.thumb_pic != null)
				return false;
		} else if (!thumb_pic.equals(other.thumb_pic))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (touser == null) {
			if (other.touser != null)
				return false;
		} else if (!touser.equals(other.touser))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BorrowInfoEntity [id=" + id + ", pid=" + pid + ", fromuser=" + fromuser + ", touser=" + touser + ", address=" + address
				+ ", cade=" + cade + ", phone=" + phone + ", land_name=" + land_name + ", qq=" + qq + ", email=" + email + ", borrow_name="
				+ borrow_name + ", return_name=" + return_name + ", title=" + title + ", description=" + description + ", borrow_num="
				+ borrow_num + ", br_date=" + br_date + ", re_date=" + re_date + ", addtime=" + addtime + ", bx_where=" + bx_where
				+ ", thumb_pic=" + thumb_pic + ", shstate=" + shstate + ", borrow_unit=" + borrow_unit + ", borrow_price=" + borrow_price
				+ ", province_id=" + province_id + ", city_id=" + city_id + ", county_id=" + county_id + ", click_num=" + click_num
				+ ", isread=" + isread + ", isignord=" + isignord + ", restate=" + restate + "]";
	}

}
