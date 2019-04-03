package info.studyup.studyupserver.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gs.fw.common.mithra.finder.Operation;

import info.studyup.domain.Member;
import info.studyup.domain.MemberFinder;

@Service
@Transactional(readOnly = true)
public class MemberService {

    public Member getMember(Integer memberId) {
        Operation operation = MemberFinder.memberId().eq(memberId);
        return MemberFinder.findOne(operation);
    }

    public Member getMember(String mailAddress, String password) {
        Operation operation = MemberFinder.mailAddress().eq(mailAddress);
        Member member = MemberFinder.findOne(operation);

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
