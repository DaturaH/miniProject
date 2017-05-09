package com.miniProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.miniProject.service.ImServiceI;


@Controller
@RequestMapping("/imcontroller")
public class ImController {
	@Autowired
	private ImServiceI imservice;
	
	@RequestMapping(value="/create")
	@ResponseBody
	public String create() throws Exception{
		//return imservice.createChatRoom("hellworld1","hutianqi");
		return imservice.create("dashen1");
	}
	
	@RequestMapping(value="/createChatRoom")
	@ResponseBody
	public String createChatRoom() throws Exception{
		return imservice.createChatRoom("dashen","dashenchatroom" ,"" ,"" ,"");
		//return imservice.create("dashen");
	}
}
