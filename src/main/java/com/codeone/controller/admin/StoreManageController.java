package com.codeone.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeone.dto.blog.BlogDto;
import com.codeone.dto.store.StoreItemDto;
import com.codeone.service.admin.StoreManageService;

@RestController
@RequestMapping(value="/storemanage")
public class StoreManageController {
	
	@Autowired
	StoreManageService service;
	
	@GetMapping("/getAllStoreMng")
	public ResponseEntity<Map<String, Object>> getAllStoreMng () {
		System.out.println("getAllStoreMng");
	
		Map<String, Object> map = new HashMap<>();        
	    try {
	    	List<StoreItemDto> result = service.getAllStoreMng();
	        map.put("list", result);
	        return ResponseEntity.ok(map);                  // 성공
	        
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().build();      // 정보 가져오기 실패
	    }
	}
	
	@PostMapping("/showHideStore")
	public ResponseEntity<Void> showHideStore(@RequestParam int seq, @RequestParam int delflg) {
	    System.out.println("showHideStore");
	    if (service.showHideStore(seq, delflg)) {
	        return ResponseEntity.ok().build();
	    } else {
	        return ResponseEntity.noContent().build();
	    }
	}

	
}