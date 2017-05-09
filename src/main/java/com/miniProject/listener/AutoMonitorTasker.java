package com.miniProject.listener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.miniProject.model.UserSubcHouseLiveForm;
import com.miniProject.service.AgencyScheduleServiceI;
import com.miniProject.service.PushServiceI;
import com.miniProject.util.RetMapUtils;

@Component("autoMonitorTasker")
public class AutoMonitorTasker extends TimerTask {
	@Autowired
	private PushServiceI pushService;
	
	@Autowired
	private AgencyScheduleServiceI agencyscheduleservice;
	
	
	Logger logger = LoggerFactory.getLogger(TaskContextBeanPostProcessor.class);

	
	@Override
	public void run() {
		try {
			timeMessage();
		} catch (SQLException e) {
			e.printStackTrace();
		}
   	}
	
    public void timeMessage() throws SQLException{
    	Connection conn = null;
        String sql;	
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");             
        // 避免中文乱码要指定useUnicode和characterEncoding
        String url = "jdbc:mysql://223.252.223.46:3306/mini_project?"
                + "user=root&password=dashenwudi&useUnicode=true&characterEncoding=UTF8";
		List<Map<String , Object>> subList = new ArrayList<Map<String , Object>>();

        try {
            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动 
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            sql = "select * from tbl_live_sub_info where sub_begin_time between date_add(NOW()  , interval + 2 minute) and date_add(NOW()  , interval + 10 minute) and verify_flag = 1 group by pub_user_id";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
				Map<String , Object> subResult = new HashMap<>();
				subResult.put("liveSubInfoId" ,rs.getString(1) );
				subResult.put("houseId" , rs.getString(2));
				subResult.put("pubUserId" , rs.getString(3));
				subResult.put("subUserId" , rs.getString(4));
				subResult.put("subBeginTime" , df1.parse(rs.getString(5)));
				//subResult.put("subEndTime" , df1.parse(rs.getString(6)));
				subResult.put("verifyFlag" , rs.getInt(7));
				subList.add(subResult);
                logger.info(rs.getString(1) + "\t" + rs.getString(2) + "\t" +rs.getString(3) + "\t" +rs.getString(4) + "\t" +rs.getString(5) + "\t" +rs.getString(6) + "\t" +rs.getInt(7));// 入如果返回的是int类型可以用getInt()
            }
            
        } catch (SQLException e) {
            logger.error("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    	
//    	System.out.println("hah");
    }
}