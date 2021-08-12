package com.example.client.dto;

import lombok.Data;

@Data
public class Req<T> {

	private Header header;
	private T responseBody;
	
	@Data
	public static class Header{
		private String resCode;
	}
}
