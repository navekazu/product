package info.studyup.studyupserver.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import info.studyup.domain.Member;

@Service
@Transactional(readOnly = true)
public class MemberService {

    public Member getMember(Integer memberId) {
        return null;
    }
    public Member getMember(String mailAddress, String password) {
        return null;
    }

    @Transactional(readOnly = false)
    public Member addMember(Member member) {
        return member;
    }
    @Transactional(readOnly = false)
    public Member updateMember(Integer memberId, Member member) {
        return member;
    }
    @Transactional(readOnly = false)
    public Member deleteMember(Integer memberId) {
        return null;
    }
}
