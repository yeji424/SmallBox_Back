package com.movie.smallbox.service;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movie.smallbox.dao.LoginDao;
import com.movie.smallbox.dao.MemberDao;
import com.movie.smallbox.dto.Login;
import com.movie.smallbox.dto.Member;
import com.movie.smallbox.dto.SaltInfo;
import com.movie.smallbox.dao.SaltDao;
import com.movie.smallbox.util.OpenCrypt;
@Service
public class MemberService {
	
	@Autowired
	MemberDao memberDao;
	
	@Autowired
	LoginDao loginDao;
	
	@Autowired
	SaltDao saltDao;
	
	public Login tokenLogin(Member m) throws Exception {
		String email=m.getEmail();
		
		// 0. email로 userId 조회
		Integer userId = memberDao.getUserIdByEmail(email);
		
		// 회원 존재 여부 먼저 확인
	    if (userId == null) {
	        throw new Exception("존재하지 않는 이메일입니다. 회원가입을 먼저 해주세요.");
	    }
	    
	    // 계정 잠김 확인
	    Integer lockedStatus = loginDao.isAccountLocked(email);
	    boolean isLocked = lockedStatus != null && lockedStatus == 1;
	    
	    // 잠겼으면
	    if (isLocked) {
	        Date lastAttempt = loginDao.getLastAttempt(email);
	        long lockTime = (new Date().getTime() - lastAttempt.getTime()) / (1000 * 60);

	        if (lockTime < 10) {
	            throw new Exception("로그인 횟수 초과로 계정이 잠겼습니다. " + (10 - lockTime) + "분 후 다시 시도해주세요.");
	        } else {
	            loginDao.resetLoginAttempt(email); // 10분 후 계정 잠금 해제 (잠금 여부 삭제)
	        }
	    }
	    
	    // 1. userId로 salt 찾아옴
		SaltInfo saltInfo=saltDao.selectSalt(userId);
		// 2. pwd 가져와서
		String pwd = m.getPwd();
		// 3. 암호화 pwd 만들기
		byte [] pwdHash=OpenCrypt.getSHA256(pwd, saltInfo.getSalt());
		String pwdHashHex=OpenCrypt.byteArrayToHex(pwdHash);
		m.setPwd(pwdHashHex); // 해싱된 값으로 패스워드 다시 저장
		m.setUserId(userId); // userId도 저장
		
		m=memberDao.tokenLogin(m);
		
		// 비번 틀렸으면
		if(m == null) {
		    loginDao.updateLoginAttempt(email); // 시도 횟수 증가

		    // 업데이트된 로그인 시도 횟수 다시 조회 (최신 값 가져오기)
		    Integer attemptCount = loginDao.getLoginAttemptCount(email);
			// 5번째 시도에서 틀리면 즉시 계정 잠금
			if(attemptCount != null && attemptCount >= 5) {
				loginDao.lockAccount(email);
				throw new Exception("로그인 횟수 초과로 계정이 잠겼습니다.");
			}
			
			throw new Exception("이메일 또는 비밀번호가 올바르지 않습니다.");
		} else {
			loginDao.resetLoginAttempt(email);
			String userName=m.getUserName();
			if(userName!=null && !userName.trim().equals("")) {
				//member table에서 email로 확인한 userId과 pwd가 확인된 상황 즉 login ok

				// 해시토큰 생성
				// 1. salt 생성
				String salt=UUID.randomUUID().toString();
				// System.out.println("salt : " +salt);
				// 2. email hashing
				byte[] originalHash=OpenCrypt.getSHA256(String.valueOf(userId), salt);
				// 3. db에 저장하기 좋은 포맷으로 인코딩
				String token=OpenCrypt.byteArrayToHex(originalHash);
				// System.out.println("token : "+token);
				// 4. login table에 token 저장
				Login loginInfo=new Login(userId, token, userName, new Date());
				loginDao.insertToken(loginInfo);
				return loginInfo;
			}
		}
		
		return null;		 
	}
	
	public void insertMember(Member m) throws Exception{
		
		// email 유효성 검사
		String email = m.getEmail();
		if(!isValidEmail(email)) {
			throw new Exception("유효하지 않은 이메일 형식입니다.");
		}
		
		// pwd 유효성 검사
		String pwd = m.getPwd();
		if(!isValidPwd(pwd)) {
			throw new Exception("패스워드는 8자리 이상이어야 하며, 특수문자와 숫자를 포함해야 합니다.");
		}
		
		try {
			// pwd 암호화
			// 1. salt 생성
			String salt = UUID.randomUUID().toString();
			// System.out.println("salt:"+salt);
			// 2. pwd hashing
			byte[] originalHash=OpenCrypt.getSHA256(pwd, salt);
			// 3. db에 저장하기 좋은 포맷으로 인코딩
			String pwdHash=OpenCrypt.byteArrayToHex(originalHash);
			// System.out.println("pwdHash : "+pwdHash);
			// 4. pwd를 pwdHash값으로 저장
			m.setPwd(pwdHash);
			// 5. saltInfo table에 salt정보 따로 저장
			//    주의 : insertMember먼저 하고 insertSalt 진행 (email 때문에)
			memberDao.insertMember(m);
			
			// 6. 입력한 email값으로 자동생성된 userId 가져와서
			int userId = memberDao.getUserIdByEmail(email);
			
			// 7. salt값 저장
			saltDao.insertSalt(new SaltInfo(userId, salt));
			
		} catch (org.springframework.dao.DuplicateKeyException e) {
	        throw new Exception("이미 가입된 이메일입니다."); // 이메일 중복 오류 반환
	    } 
		catch (Exception e) {
			throw new Exception("회원가입 중 서버 오류가 발생했습니다.");
		}

	}
	
	public void updateMember(Member m) throws Exception{
		memberDao.updateMember(m);
	}
	
	public void deleteMember(int userId) throws Exception{
		memberDao.deleteMember(userId);
	}
	// authorization = token = sessionId
    public void logout(int userId) throws Exception {
        loginDao.deleteToken(userId);
    }
	// 로그인 타임 가져오기
    public Date getLoginTimeByToken(String authorization) throws Exception {
        Date loginTime = loginDao.getLoginTimeByToken(authorization);

    	
    	// System.out.println("**** MemberService : 받은 토큰 : " + authorization);
        // System.out.println("**** MemberService : LoginTIme 조회 : " + loginTime);

    	
    	return loginTime;
    }
    
    public int getUserIdFromToken(String authorization) throws Exception {
        Integer userId = loginDao.getUserIdByToken(authorization);

        // System.out.println("**** MemberService : 받은 토큰 : " + authorization);
        // System.out.println("**** MemberService : userId 조회 : " + userId);

        if (userId == null) {
            // System.out.println("**** MemberService: 유효하지 않은 토큰");
            throw new Exception("유효하지 않은 토큰입니다.");
        }
        return userId;
    }
    
	public void updateLoginTime(String authorization) throws Exception {
		loginDao.updateLoginTime(authorization);
	}
    
	/** 유효성 검사 메서드
	 * email 유효성 검사 메서드
	 * pwd 유효성 검사 메서드
	 */
	
	// 이메일 유효성 검사 메서드
	private boolean isValidEmail(String email) {
	    // 이메일 패턴
	    String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	    return Pattern.matches(emailPattern, email);
	}
	
	// 패스워드 유효성 검사 메서드
	private boolean isValidPwd(String pwd) {
	    // 패스워드 패턴: 8자리 이상, 숫자 포함, 특수문자 포함
	    String pwdPattern = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
	    return Pattern.matches(pwdPattern, pwd);
	}

}