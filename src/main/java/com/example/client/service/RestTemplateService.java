package com.example.client.service;

import java.net.URI;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.client.dto.Req;
import com.example.client.dto.UserRequest;
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
		System.out.println("@ result1: "+result1.toString());

		
//		ResponseEntity<String> result2 = rt.getForEntity(uri, String.class);
		ResponseEntity<UserResponse> result2 = rt.getForEntity(uri, UserResponse.class);
		System.out.println("@ HTTP CODE확인: "+result2.getStatusCode());
		System.out.println("@ BODY 확인: "+result2.getBody());
		
		return result2.getBody();
	}
	
	public void post() {
		
		URI uri = UriComponentsBuilder
				.fromUriString("http://localhost:9090")
				.path("/api/server/user/{userId}/name/{userName}")
				.encode()
				.build()
				//위 PathVariable과 expand()안 콤마로 순서대로 매칭
				.expand("wony", "choi")
				.toUri();
		
		System.out.println("@ uri.toString(): " + uri.toString());
		
		UserRequest req = new UserRequest();
		req.setAge(44);
		req.setName("최정원");
		
		RestTemplate rt = new RestTemplate();
		ResponseEntity<UserResponse> res = rt.postForEntity(uri, req, UserResponse.class);
		System.out.println("@ HTTP CODE확인: "+res.getStatusCode());
		System.out.println("@ HTTP Header확인: "+res.getHeaders());
		System.out.println("@ BODY 확인: "+res.getBody());
	}

	public UserResponse exchange() {
		
		URI uri = UriComponentsBuilder
				.fromUriString("http://localhost:9090")
				.path("/api/server/user/{userId}/name/{userName}")
				.encode()
				.build()
				//위 PathVariable과 expand()안 콤마로 순서대로 매칭
				.expand("wony", "choi")
				.toUri();
		
		System.out.println("@ uri.toString(): " + uri.toString());
		
		UserRequest userReq = new UserRequest();
		userReq.setAge(44);
		userReq.setName("최정원");
		
		// requestEntity로 header에 원하는 데이터를 넣어서 보낼 수 있다.
		RequestEntity<UserRequest> reqEntity = RequestEntity
				.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.header("x-authorization", "abc")
				.header("custom-header", "ABC")
				.body(userReq);
				
		RestTemplate resTemplate = new RestTemplate();
		ResponseEntity<UserResponse> res = resTemplate.exchange(reqEntity, UserResponse.class);
		return res.getBody();
		
	}
	
	// 내가 원하는 Req<UserResponse> 타입의 JSON형태 주고 받기
	/*{
	    "header": {
	        "resCode": null
	    },
	    "responseBody": {
	        "name": "가나다",
	        "age": 55
	    }
	}*/
	public Req<UserResponse> genericExchange() {
		
		URI uri = UriComponentsBuilder
				.fromUriString("http://localhost:9090")
				.path("/api/server/user/{userId}/name/{userName}")
				.encode()
				.build()
				//위 PathVariable과 expand()안 콤마로 순서대로 매칭
				.expand("wony", "choi")
				.toUri();
		
		System.out.println("@ uri.toString(): " + uri.toString());
		
		UserRequest userReq = new UserRequest();
		userReq.setAge(55);
		userReq.setName("가나다");
		
		Req<UserRequest> req = new Req<>();
		req.setHeader(new Req.Header());
		req.setResponseBody(userReq);
		
		// requestEntity로 header에 원하는 데이터를 넣어서 보낼 수 있다.
		RequestEntity<Req<UserRequest>> reqEntity = RequestEntity
				.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.header("x-authorization", "abc")
				.header("custom-header", "ABC")
				.body(req);
		
		RestTemplate resTemplate = new RestTemplate();
		// 제네릭에는 class를 사용할 수 없다 즉, Req<UserResponse>.class -> 오류발생 -> 따라서 RestTemplate의 ParameterizedTypeReference를 사용해야한다. 
		// ResponseEntity<Req<UserResponse>> res = resTemplate.exchange(reqEntity, Req<UserResponse>.class);
		ResponseEntity<Req<UserResponse>> res = resTemplate.exchange(reqEntity, new ParameterizedTypeReference<Req<UserResponse>>(){});
		return res.getBody();
		
	}
	
	
}
