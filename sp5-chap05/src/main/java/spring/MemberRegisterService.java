package spring;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MemberRegisterService {
	
	@Autowired
	private MemberDao memberDao;
	
	public MemberRegisterService(MemberDao memberDao) {
		this.memberDao=memberDao;
	}
	
	public MemberRegisterService() {}
	
	public Long regist(RegisterRequest req) {
		Member member = memberDao.selectByEmail(req.getEmail());
		if(member!=null) {
			throw new DuplicateMemberException("duplicated Email");
		}
		Member newMember=new Member(
				req.getEmail(),req.getPassword(),req.getName(),
				LocalDateTime.now());
		memberDao.insert(newMember);
		
		return newMember.getId();
	}
}
