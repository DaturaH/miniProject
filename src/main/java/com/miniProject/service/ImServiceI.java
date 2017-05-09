package com.miniProject.service;

public interface ImServiceI {
	String create(String accid) throws Exception;
	String update(String accid, String props , String token) throws Exception;
	String refreshToken(String accid) throws Exception;
	String block(String accid) throws Exception;
	String unblock(String accid) throws Exception;
	String updateUinfo(String accid , String name , String icon , String sign , String email , String birth ,String mobile ,String gender , String ex) throws Exception;
	String getUinfos(String accids) throws Exception;
	
	String sendMsg(String from , String ope , String to , String type , String body , String option) throws Exception;
//聊天室功能	
	String createChatRoom(String creator , String name , String announcement	, String broadcasturl , String ext	) throws Exception;
	String getChatRoom(String roomid , String needOnlineUserCount) throws Exception;
	String updateChatRoom(String roomid , String name , String announcement	, String broadcasturl , String ext , String needNotify	, String notifyExt) throws Exception; 
	String toggleCloseStatChatRoom(String roomid , String operator , String valid) throws Exception;
	String setMemberRoleChatRoom(String roomid , String operator , String target , String opt , String optvalue) throws Exception;
	String requestAddrChatRoom(String roomid , String accid , String clienttype) throws Exception;
	String sendMsgChatRoom(String roomid , String msgld, String fromAccid , String msgType , String resendFlag , String attach, String ext) throws Exception;
	String history(String from , String to , String begintime , String endtime , String limit , String reverse) throws Exception;
}















