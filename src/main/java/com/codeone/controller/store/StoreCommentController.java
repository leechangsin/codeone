package com.codeone.controller.store;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codeone.dto.store.StoreCommentDto;
import com.codeone.dto.store.StoreCommentParam;
import com.codeone.dto.store.StoreParam;
import com.codeone.service.store.StoreCommentService;

@RestController
public class StoreCommentController {
	
	@Autowired
	StoreCommentService service;
	
	// 중고거래 댓글 작성
	@PostMapping(value = "/storeComment")
	public ResponseEntity<Void> writeCommentStore(@RequestBody StoreCommentDto dto){
		System.out.println("StoreCommentController writeCommentStore()" + new Date());
		
//		String id = "mi538";
//		dto.setId(id);
		System.out.println(dto);
		
		boolean isStoreComment = service.writeCommentStore(dto);
		
		if(isStoreComment) {
			return ResponseEntity.ok().build();		// 댓글작성 성공
		}
		return ResponseEntity.badRequest().build(); // 댓글작성 실패
	}
	
	// 중고거래 댓글 목록
	@GetMapping(value = "/storeComment")
	public ResponseEntity<Map<String, Object>> getStoreCommentList(StoreCommentParam param){
		System.out.println("StoreCommentController getStoreCommentList()" + new Date());
		
		// param == itemseq, start, end, pageNumber
		
		// 글의 시작과 끝
		// 0부터 시작하기떄문에 리액트에서 넘겨줄 때 -1해서 넘겨줌
		int pn = param.getPageNumber();	// 0 1 2 3 4
		
//		int start = 1 + ( pn * 10);	// 1  11
//		int dataCount = ( pn + 1 ) * 10;	// 10 20
		
		int start = pn * 10; // 0 10 20 30 40	
		
		param.setStart(start);		// 페이지 넘버에 따라 보여줄 시작 행 위치바뀜
		param.setDataCount(10);		// 보여줄데이터 10개씩설정		
		
//		int itemseq = param.getItemseq();
		System.out.println(param);
		// 댓글목록
		List<StoreCommentDto> commentList = service.getStoreCommentList(param);	// 
		
		// 댓글의 총갯수
		int totalCount = service.getStoreCommentCount(param.getItemseq());	// search, choice 들어오는값은 없음.
		
		
		// 목록이랑 글의 총갯수 같이 넘겨주기
		
		if(commentList.size() == 0) {
			return ResponseEntity.noContent().build();		// 작성된 댓글없음		
		}
		Map<String, Object> map = new HashMap<>();
		map.put("commentlist", commentList);
		// map.put("pageBbs", pageBbs);
		map.put("cnt", totalCount);	// 리액트 글의 총수 보내주기
		
		return ResponseEntity.ok(map);
		
	}
	
	// 댓글 수정
	@PutMapping(value = "/storeComment")
	public ResponseEntity<Void> updateStoreComment(@RequestBody StoreCommentDto dto){
		System.out.println("StoreCommentController storeComment()" + new Date());
		System.out.println(dto);
		
		try {
			service.updateStoreComment(dto);
			return ResponseEntity.ok().build();		// 댓글수정 성공
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().build(); // 댓글작성 실패
		}
	}
	
	// 댓글 삭제
	@DeleteMapping(value = "/storeComment")
	public ResponseEntity<Void> deleteStoreComment(int seq){
		System.out.println("StoreCommentController deleteStoreComment()" + new Date());
		
		try {
			service.deleteStoreComment(seq);
			return ResponseEntity.ok().build();		// 댓글삭제 성공
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().build(); // 댓글삭제 실패
		}
	}
	

}
