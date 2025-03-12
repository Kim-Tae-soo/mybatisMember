package kr.or.ddit.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


public class MyBatisUtil {
	private static SqlSessionFactory factory = null;
	
	static{
		InputStream in = null;
		try {
			in = Resources.getResourceAsStream("kr/or/ddit/mybatis/config/mybatis-config.xml");
			factory = new SqlSessionFactoryBuilder().build(in);
			
		} catch (Exception e) {
			System.out.println("mybatis 초기화 실패");
			e.printStackTrace();
		} finally {
			if(in != null) try {in.close();} catch(IOException e){}
		}
	}
	
	// SqlSession 객체를 반환하는 메서드
	public static SqlSession getsqlSession() {
		SqlSession session = factory.openSession();
		return session;
		
	}
	// 매개변수 autoCommit이 true 이면 AutoCommit이 활성화된 상태
	// 					false이면 AutoCommit이 비활성화된 상태가 된다.
	public static SqlSession getsqlSession(boolean autoCommit) {
		SqlSession session = factory.openSession(autoCommit);
		return session;
		
	}
	
}
