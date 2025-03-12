package kr.or.ddit.mvc.Controller;

import java.util.List;
import java.util.Scanner;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.or.ddit.mvc.Service.IMemberService;
import kr.or.ddit.mvc.Service.MemberServiceImpl;
import kr.or.ddit.mvc.VO.MemberVO;
import kr.or.ddit.util.MyBatisUtil;

public class MemberController {
    private Scanner sc = new Scanner(System.in);
    private IMemberService serivce = MemberServiceImpl.getInstance();
    private SqlSessionFactory factory = null;
    private SqlSession session = null;

    public static void main(String[] args) {
        new MemberController().startMember();
    }

    public void insertMember() {
        System.out.println();
        System.out.println("회원 정보 추가하기");
        
        try {
            session = MyBatisUtil.getsqlSession();
            int cnt = 0;
            String memId = null;

            do {
                System.out.print("회원 아이디 입력 >> ");
                memId = sc.next();
                cnt = session.selectOne("mymbatisMember.getMemberIdCount", memId);
                if (cnt > 0) {
                    System.out.println("입력하신 회원 아이디 " + memId + "는 이미 사용중인 아이디 입니다.");
                    System.out.println("다른 아이디를 입력해주세요.");
                    System.out.println();
                }
            } while (cnt > 0);

            System.out.print("회원 비밀번호 입력 >> ");
            String pass = sc.next();

            System.out.print("회원 이름 입력 >> ");
            String name = sc.next();

            System.out.print("회원 전화번호 입력 >> ");
            String tel = sc.next();

            System.out.print("회원 주소 입력 >> ");
            String addr = sc.next();

            MemberVO memberVO = new MemberVO();
            memberVO.setMem_id(memId);
            memberVO.setMem_pass(pass);
            memberVO.setMem_name(name);
            memberVO.setMem_tel(tel);
            memberVO.setMem_addr(addr);

            int insertCnt = session.insert("mymbatisMember.insertMember", memberVO);

            if (insertCnt > 0) {
                session.commit();
                System.out.println("성공");
            } else {
                System.out.println("실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void deleteMember() {
        System.out.println();
        System.out.println("삭제할 회원 정보를 입력해주세요.");
        System.out.print("삭제할 회원 아이디 입력 >> ");
        String memId = sc.next();

        try {
            session = MyBatisUtil.getsqlSession();
            int cnt = session.selectOne("mymbatisMember.getMemberIdCount", memId);
            if (cnt == 0) {
                System.out.println(memId + "는(은) 존재하지 않는 회원 아이디 입니다.");
                System.out.println();
                return;
            }

            int deleteCnt = session.delete("mymbatisMember.deleteMember", memId);
            if (deleteCnt > 0) {
                session.commit();
                System.out.println("성공");
            } else {
                System.out.println("실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void updateMember() {
        session = MyBatisUtil.getsqlSession();
        System.out.println();
        System.out.println("회원 정보를 수정하는 페이지입니다.");
        System.out.println("수정할 회원 아이디를 입력해주세요 >> ");
        String memId = sc.next();

        // memId가 존재하는지 확인
        Integer cnt = session.selectOne("mymbatisMember.getMemberIdCount", memId);
        if (cnt == null || cnt == 0) {
            System.out.println(memId + "은(는) 존재하지 않는 ID입니다.");
            System.out.println();
            return;
        }

        System.out.println();
        System.out.print("수정할 비밀번호 >> ");
        String pass = sc.next();

        System.out.print("수정할 이름 >> ");
        String name = sc.next();

        System.out.print("수정할 전화번호 >> ");
        String tel = sc.next();

        System.out.print("수정할 주소 >> ");
        String addr = sc.next();

        // 입력한 자료를 VO에 저장한다.
        MemberVO memberVO = new MemberVO();
        memberVO.setMem_id(memId);
        memberVO.setMem_pass(pass);
        memberVO.setMem_name(name);
        memberVO.setMem_tel(tel);
        memberVO.setMem_addr(addr);
        
        System.out.println(memberVO);
        int updateCnt = session.update("mymbatisMember.updateMember", memberVO);
        System.out.println("updateCnt: " + updateCnt);
        

        if (updateCnt > 0) {
        	session.commit(); 
            System.out.println("성공");
        } else {
            System.out.println("실패");
        }

        session.close(); // 세션을 마지막에 닫음
    }



    private void editMemberDetails() {
        System.out.println("이건 안되지롱 ㅋ");
    }

    private void selectMember() {
    
    	List<MemberVO> memList = serivce.getAllMember();
    	System.out.println();
		System.out.println("\t--MEMBER LIST--\t");
		System.out.println("------------------------------------");
		System.out.println("ID\tPASSWORD\tNAME\tTEL\t\tADDR");
		System.out.println();
		
		if(memList==null || memList.size() == 0) {
			System.out.println("There are no registered members");
		} else {
			for(MemberVO memVO : memList) {
				String memId = memVO.getMem_id();
				String memPass = memVO.getMem_pass();
				String memName = memVO.getMem_name();
				String memTel = memVO.getMem_tel();
				String memAddr = memVO.getMem_addr();
				
				System.out.println(memId + "\t" + memPass + "\t\t" + memName + "\t" + memTel + "\t" + memAddr);
			}
		}
		
		System.out.println("------------------------------------");
		
    }

    private int displayMenu() {
        System.out.println();
        System.out.println("회원 정보 관리");
        System.out.println("1. 회원 정보 추가");
        System.out.println("2. 회원 정보 삭제");
        System.out.println("3. 회원 정보 수정");
        System.out.println("4. 회원 정보 세부 수정");
        System.out.println("5. 회원 전체 조회");
        System.out.println("0. 프로그램 종료");
        System.out.print("작업 번호 입력 >> ");

        return sc.nextInt();
    }

    public void startMember() {
        while (true) {
            int choice = displayMenu();

            switch (choice) {
                case 1:
                    insertMember();
                    break;
                case 2:
                    deleteMember();
                    break;
                case 3:
                    updateMember();
                    break;
                case 4:
                    editMemberDetails();
                    break;
                case 5:
                    selectMember();
                    break;
                case 0:
                    System.out.println("프로그램 종료");
                    return;
                default:
                    System.out.println("잘못된 메뉴 선택 재입력 해주세요");
            }
        }
    }
}
