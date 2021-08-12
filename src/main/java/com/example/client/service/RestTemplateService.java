package com.example.client.service;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.client.dto.UserResponse;

@Service
public class RestTemplateService {
	
	public UserResponse hello() {
		//클라이언트이므로 주소를 만들어서 리턴해야한다 -> 이때 URI컴포넌트를 주로 사용함.
		URI uri = UriComponentsBuilder
				.fromUriString("http://localhost:9090")
				.path("/api/server")
				//쿼리파람을 사용할 수 있다.
				.queryParam("name", "신경숙")
				.queryParam("age", 22)
				.encode()
				.build()
				.toUri();
		System.out.println("@ uri.toString(): " + uri.toString());
		
		/* =====================================================================
		 * getForObject()와 getForEntity()의 차이
		 * 여기서 get은 가져온다의 get이 아니라 HTTP GET 메서드의 get이다.
		 * getForObject(): Object형태
		 * getForEntity(): Entity형태로 getStatusCode(), getBody()를 확인할 수 있어 유용
		 * 
		 * =====================================================================
		 * 두 방식 다 동일한 결과를 나타냄
		 * 1. String으로 테스트
		 * 호출: http://localhost:8083/api/client
		 * 결과: 안녕 나는 클라이언트야, 내가 사라져볼께 얍! 안녕 나는 서버야
		 * 
		 * 2. UserResponse로 json 받기
		 * 호출: http://localhost:8083/api/client
		 * 결과: {
		 *		    "name": "신경숙",
		 *		    "age": 22
		 *		}
		 */
		
		// 상세정보를 알기 위해서 ResponseEntity를 받는 것을 추천
		RestTemplate rt = new RestTemplate();
		
//		String result1 = rt.getForObject(uri, String.class);
		UserResponse result1 = rt.getForObject(uri, UserResponse.class);

		
//		ResponseEntity<String> result2 = rt.getForEntity(uri, String.class);
		ResponseEntity<UserResponse> result2 = rt.getForEntity(uri, UserResponse.class);
		System.out.println("@ HTTP CODE확인: "+result2.getStatusCode());
		System.out.println("@ BODY 확인: "+result2.getBody());
		
		return result2.getBody();
	}
}
