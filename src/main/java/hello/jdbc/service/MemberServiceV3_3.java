package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * 트랜잭션 - @Transactional AOP
 * 트랜잭션 매니저를 호출하는 코드마저 @Transactional annotation을 통해 Proxy를 자동생성하여 해결해줌
 * @Transactional은 스프링이 관리하는 annotation이고, 해당 어노테이션을 읽고 CGLIB(JDK dynamic을 사용하지 않는 기본설정값 기준)을 생성해주는것도 스프링의 기능
 * 고로 스프링 컨테이너 안에서 작동하는 기능이기 때문에 해당 기능을 위해선 스프링 내부 환경에서 작동시켜야 한다.
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_3 {

    private final MemberRepositoryV3 memberRepository;


    @Transactional
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);
        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
