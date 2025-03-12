package kr.or.ddit.mvc.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.or.ddit.util.*;
import kr.or.ddit.mvc.VO.MemberVO;
import kr.or.ddit.util.DBUtil;

public class MemberDAOImpl implements IMemberDAO {
	
	// 1번
	private static MemberDAOImpl DAO;
	
	// 2번
	private MemberDAOImpl() {
		
	}
	
	// 3번
	public static MemberDAOImpl getInstance() {
		if(DAO == null) DAO = new MemberDAOImpl();
		return DAO;
	}

	@Override
	public int insertMember(MemberVO VO) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int cnt = 0;	// 반환값
		
		try {
			conn = DBUtil3.getConnection();
			String InsertSQL = "INSERT INTO MYMEMBER "
					+ "(MEM_ID, MEM_PASS, MEM_NAME, MEM_TEL, MEM_ADDR)"
					+ "VALUES(?,?,?,?,?)";
			pstmt = conn.prepareStatement(InsertSQL);
			pstmt.setString(1, VO.getMem_id());
			pstmt.setString(2, VO.getMem_pass());
			pstmt.setString(3, VO.getMem_name());
			pstmt.setString(4, VO.getMem_tel());
			pstmt.setString(5, VO.getMem_addr());
			
			cnt = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) try {pstmt.close();} catch(SQLException e) {}
			if(conn!=null) try {conn.close();} catch(SQLException e) {}
		}
		
		
		return cnt;
	}

	@Override
	public int deleteMember(String memId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int cnt = 0;
		
		try {
			conn = DBUtil3.getConnection();
			String DeleteSQL = "DELETE FROM MYMEMBER WHERE MEM_ID = ?";
			pstmt = conn.prepareStatement(DeleteSQL);
			pstmt.setString(1, memId);
			
			cnt = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();		
		} finally {
			if(pstmt!=null) try {pstmt.close();} catch(SQLException e) {}
			if(conn!=null) try {conn.close();} catch(SQLException e) {}
		}
		
		return cnt;
	}

	@Override
	public int updateMember(MemberVO VO) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int cnt = 0;
		try {
			conn = DBUtil3.getConnection();
			String UpdateSQL = "UPDATE MYMEMBER SET "
					+ "MEM_PASS = ?, MEM_NAME = ?, MEM_TEL = ?, MEM_ADDR = ? "
					+ "WHERE MEM_ID = ?";
	
			pstmt = conn.prepareStatement(UpdateSQL);
			pstmt.setString(5, VO.getMem_id());
			pstmt.setString(1, VO.getMem_pass());
			pstmt.setString(2, VO.getMem_name());
			pstmt.setString(3, VO.getMem_tel());
			pstmt.setString(4, VO.getMem_addr());
			
			cnt = pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) try {pstmt.close();} catch(SQLException e) {}
			if(conn!=null) try {conn.close();} catch(SQLException e) {}
		}
		return cnt;
	} 

	@Override
	public List<MemberVO> getAllMember() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<MemberVO> memList = null;	//반환값이 저장될 변수
		
		try {
			conn = DBUtil3.getConnection();
			String SelectSQL = "SELECT * FROM MYMEMBER ORDER BY 1";
			
			pstmt = conn.prepareStatement(SelectSQL);
			rs = pstmt.executeQuery();
			
			// 반복문 안에서 select한 결과를 한 개의 레코드 단위로 VO에 저장한다.
			// VO의 저장이 완료되면 이 VO룰 List에 추가한다.
			while (rs.next()) {
				if (memList == null) {
					// List 객체 생성
					memList = new ArrayList<MemberVO>();
				}
				// VO객체를 생성한 후 ResultSet객체에서 데이터를 꺼내 VO에 데이터를 저장한다.
				MemberVO memVO = new MemberVO();
				memVO.setMem_id(rs.getString("MEM_ID"));
				memVO.setMem_pass(rs.getString("MEM_PASS"));
				memVO.setMem_name(rs.getString("MEM_NAME"));
				memVO.setMem_tel(rs.getString("MEM_TEL"));
				memVO.setMem_addr(rs.getString("MEM_ADDR"));
				
				memList.add(memVO);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try {rs.close();} catch(SQLException e) {}
			if(pstmt!=null) try {pstmt.close();} catch(SQLException e) {}
			if(conn!=null) try {conn.close();} catch(SQLException e) {}
		}
		
		return memList;		//reurn을 먼저 작성해보자
	}

	@Override
	public int getMemberIdCount(String memId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int count = 0; // 반환값이 저장될 변수
		
		try {
			conn = DBUtil3.getConnection();
			String CountSQL = "SELECT COUNT(*) CNT FROM MYMEMBER WHERE MEM_ID = ?";
			pstmt = conn.prepareStatement(CountSQL);
			pstmt.setString(1, memId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				count = rs.getInt("CNT");
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try {rs.close();} catch(SQLException e) {}
			if(pstmt!=null) try {pstmt.close();} catch(SQLException e) {}
			if(conn!=null) try {conn.close();} catch(SQLException e) {}
		}
		
		return count;
	}

	@Override
	// Map의 key값 정보 ==> 회원ID(MEM_ID), 수정할 컬럼명(FIELD), 변경될데이터(DATA)
	public int EditMemberDetails(Map<String, String> paramMap) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int cnt = 0;
		
		try {
			conn = DBUtil3.getConnection();
			String DetailUpdateSQL = "UPDATE MYMEMBER SET " 
										+ paramMap.get("FIELD") + " = ?" 
										+ "WHERE MEM_ID = ? ";
			pstmt = conn.prepareStatement(DetailUpdateSQL);
			pstmt.setString(1, paramMap.get("DATA"));
			pstmt.setString(2, paramMap.get("MEMID"));
			
			cnt = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) try {pstmt.close();} catch(SQLException e) {}
			if(conn!=null) try {conn.close();} catch(SQLException e) {}
		}
		
		return cnt;
	}

	

}
