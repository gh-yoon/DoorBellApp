package com.heaven.mvc.board.domain;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Alias("boardVO")
public class BoardVO {
	private int seq;
	private String id;
	private String ip;
	private int password;
	
	public BoardVO() { }
	
	public BoardVO(String id, String ip, int password) {
		super();
		this.id = id;
		this.ip = ip;
		this.password = password;
	}
	
	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPassword() {
		return password;
	}

	public void setPassword(int password) {
		this.password = password;
	}
}
