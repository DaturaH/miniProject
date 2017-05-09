package com.miniProject.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.apache.http.impl.client.TargetAuthenticationStrategy;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.hash.Hashing;
import com.miniProject.dao.UserHouseFormMapper;
import com.miniProject.dao.UserHouseFormMongoDao;
import com.miniProject.dao.UserInfoMapper;
import com.miniProject.model.CommunityInfo;
import com.miniProject.model.UserHouseForm;
import com.miniProject.model.UserInfo;
import com.miniProject.service.HouseServiceI;
import com.miniProject.service.ImServiceI;
import com.miniProject.service.UserServiceI;
import com.miniProject.util.Config;
import com.miniProject.util.JsonResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SuppressWarnings({ "unchecked", "unchecked" })
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private ImServiceI imservice;

	@Autowired
	private UserServiceI userservice;

	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@Resource
	private HouseServiceI houseService;

	private static Logger logger = Logger.getLogger(UserController.class);


    public UserController() {

    }

    private String genToken(String name, int type,String id) {
    	return "Bearer " + Jwts.builder().setSubject(name)
        .claim("type", type).claim("uid", id).setIssuedAt(new Date())
        .signWith(SignatureAlgorithm.HS256, Config.SECRET_KEY).compact();
    }

	@Autowired
	private MongoOperations mongoOps;

	@Autowired
	private UserHouseFormMapper userHouseFormMapper;

	private static class Te{
		private int a;
		private String b;
		public int getA() {
			return a;
		}
		public void setA(int a) {
			this.a = a;
		}
		public String getB() {
			return b;
		}
		public void setB(String b) {
			this.b = b;
		}
	}
	




    @RequestMapping(value = "test")
    @ResponseBody
    public String test(String name) {
    	
//    	template.boundValueOps("key").set("hello");
//		System.out.println(template.boundValueOps("key").get());

//    	List<UserHouseForm> list = userHouseFormMapper.getAll();
//    	for(UserHouseForm e:list) {
//    		Point point = new Point(e.getLongitude(),e.getLatitude());
//    		e.setPoint(point.asArray());
//    		mongoOps.insert(e);
//    		System.out.println("------");
//
//    	}
//
    	return "test";

    }


    @RequestMapping(value = "forget_password")
    @ResponseBody
    public Map<String, Object> forgetPassword(@RequestBody final Map<String, String> request) {
    	Map<String, Object> res = new HashMap<>();
    	String username = request.get("username");
    	String password = request.get("password");
    	String code = request.get("code");
    	try {
    		UserInfo temp = userInfoMapper.selectByPhoneNumber(username);
			boolean auth = userservice.authCode(username, code);
			if(temp  == null) {
				res.put("code", 8444);
				return res;
			}
			if(!auth) {
				res.put("code", 8603);
				return res;
			}
				
			UserInfo userInfo = new UserInfo();
			userInfo.setUserId(temp.getUserId());
			userInfo.setPasswd(getDigest(password));
			userInfoMapper.updateByPrimaryKeySelective(userInfo);

			res.put("code", 8200);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.put("code", 8444);
		}
        return res;
    }


    @RequestMapping(value = "login")
    @ResponseBody
    public Response login(@RequestBody final UserLogin login) {
    	Response res = new Response();
    	LoginResponse loginResponse = new LoginResponse();
    	res.data = loginResponse;

    	UserInfo userInfo =  userInfoMapper.selectByPhoneNumber(login.username);
        if (userInfo == null || !getDigest(login.password).equals(userInfo.getPasswd())) {
        	res.code = 8601;
        	logger.debug(login+ " : " + "login failed");
            return res;
        }

        loginResponse.authtoken = genToken(userInfo.getPhoneNumber(),userInfo.getUserType(),userInfo.getUserId());
        loginResponse.token = userInfo.getToken();
        loginResponse.type = userInfo.getUserType();
        return res;
    }

    @RequestMapping(value = "sendCode")
	@ResponseBody
    public Response sendCode(@RequestBody Map<String, String> request) {
    	String phone = request.get("phone");
    	Response res = new Response();

    	try {

			if(!userservice.sendAuthCode(phone))
				throw new Exception("发送验证码失败");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug(e.getMessage(),e);
	    	res.code = 8604;
		}
		return res;
    }

    @RequestMapping(value = "sendCodeTest")
	@ResponseBody
    public Response sendCodeTest(@RequestBody Map<String, String> request) {
    	String phone = request.get("phone");
    	Response res = new Response();
    	Map<String, Object> data = new HashMap<>();
    	try {
    		String msg = userservice.sendAuthCodeTest(phone);
    		Map<String, Object> map = JsonResponse.toMap(msg);
			if(!map.get("code").equals(200))
				throw new Exception("发送验证码失败" + " code: " + map.get("code"));
    		data.put("sentCode", map.get("obj"));
    		res.data = data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug(e.getMessage(),e);
	    	res.code = 8604;
		}
		return res;
    }

    //创建云信IM帐号
    private String createIMAccount(String name) throws Exception {

		String json = imservice.create(name);
		Map<String, Object> m = JsonResponse.toMap(json);
		if(m.get("code").equals(200)) {
			Map<String, Object> info = (Map<String, Object>) m.get("info");
			return (String) info.get("token");

		} else if(m.get("code").equals(414)) {
			//已经创建云信IM账号，则刷新token
			json = imservice.refreshToken(name);
			m = JsonResponse.toMap(json);
			if(m.get("code").equals(200)) {
				Map<String, Object> info = (Map<String, Object>) m.get("info");
				return (String) info.get("token");
			}
		}
		return null;
    }



	@RequestMapping(value = "register")
	@ResponseBody
	public Response register(@RequestBody final UserRegister register) {
		Response res = new Response();

		RegisterResponse registerResponse = new RegisterResponse();
		res.data = registerResponse;
		registerResponse.type = register.type;

		if(register.type != 1 && register.type!=2 && register.type != 0) {
			res.code = 8405;
			res.msg = "invalid type";
			return res;
		}

		try {
			boolean auth = userservice.authCode(register.username, register.code);
			//验证码错误
			if(!auth) {
				res.code = 8603;
				return res;
			}
			//用户已注册
			UserInfo userInfo =  userInfoMapper.selectByPhoneNumber(register.username);
			if(userInfo != null) {
				res.code = 8602;
				return res;
			}

			userInfo = new UserInfo();
			String uid = UUID.randomUUID().toString();
			userInfo.setUserId(uid);
			String digest = getDigest(register.password);
			userInfo.setPasswd(digest);
			userInfo.setAccoutBuildTime(new Date());
			userInfo.setPortraitFileName("/static/portraitFiles/" + register.type + ".png");
			userInfo.setPhoneNumber(register.username);
			userInfo.setUserType(register.type);
			//用户昵称，默认手机号后四位
			userInfo.setUserName(userInfo.getPhoneNumber().substring(userInfo.getPhoneNumber().length()-4));

			String token = createIMAccount(register.username);

			registerResponse.token = token;
			userInfo.setToken(registerResponse.token);

			//鉴权token,注册后直接登录
			registerResponse.authtoken = genToken(register.username, register.type,uid);

			//插入到数据库
			userInfoMapper.insert(userInfo);

		} catch (Exception e) {
			res.code = 8444;
			e.printStackTrace();
		}
		logger.info(registerResponse);
		return res;
	}


	private String getDigest(String msg) {
		return Hashing.sha256().hashString(msg+Config.SALT, StandardCharsets.UTF_8).toString();
	}

	private static  class Response {
		public int code = 8200;
		public Object data;
		public String msg;
	}

    private static class UserRegister {
        @Override
		public String toString() {
			return "UserRegister [username=" + username + ", password=" + password + ", type=" + type + ", code=" + code
					+ "]";
		}
		public String username;
        public String password;
        public int type;
        public String code;
    }


    private static class UserLogin {
        @Override
		public String toString() {
			return "UserLogin [username=" + username + ", password=" + password + "]";
		}
		public String username;
        public String password;
    }


    private static class LoginResponse {
        @Override
		public String toString() {
			return "LoginResponse [token=" + token + ", type=" + type + ", authtoken=" + authtoken + "]";
		}
		public String token;
        public int type;
        public String authtoken;
    }

    private static class RegisterResponse {
        @Override
		public String toString() {
			return "RegisterResponse [token=" + token + ", authtoken=" + authtoken + ", type=" + type
					+ "]";
		}
		public String token;
		public String authtoken;
		public int type;
    }
}


