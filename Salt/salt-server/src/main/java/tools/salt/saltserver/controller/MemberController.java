package tools.salt.saltserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tools.salt.domain.Member;
import tools.salt.saltserver.service.MemberService;

@RestController
@RequestMapping("/member/")
public class MemberController {
    private final static Logger logger = LoggerFactory.getLogger(MemberController.class);

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;

    }

    // 取得
    @RequestMapping(method=RequestMethod.GET,
            value="{member-id}")
    public ResponseEntity<Member> getMember(
            @PathVariable("member-id") Integer memberId) {
        Member member = new Member();
        return ResponseEntity.ok(member);
    }

    // 追加
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<Void> addMember(
            @ModelAttribute Member member) {
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    // 更新
    @RequestMapping(method=RequestMethod.POST,
            value="{member-id}",
            headers={"X-HTTP-Method-Override=PUT"})
    public ResponseEntity<Member> updateMemberPost(
            @PathVariable("member-id") Integer memberId,
            @ModelAttribute Member member) {
        return updateMember(memberId, member);
    }
    @RequestMapping(method=RequestMethod.PUT,
            value="{member-id}")
    public ResponseEntity<Member> updateMember(
            @PathVariable("member-id") Integer memberId,
            @ModelAttribute Member member) {
        return ResponseEntity.ok(member);
    }

    // 削除
    @RequestMapping(method=RequestMethod.POST,
            value="{member-id}",
            headers={"X-HTTP-Method-Override=DELETE"})
    public ResponseEntity<Member> deleteMemberPost(
            @PathVariable("member-id") Integer memberId) {
        return deleteMember(memberId);
    }
    @RequestMapping(method=RequestMethod.DELETE,
            value="{member-id}")
    public ResponseEntity<Member> deleteMember(
            @PathVariable("member-id") Integer memberId) {
        Member member = new Member();
        return ResponseEntity.ok(member);
    }
}
