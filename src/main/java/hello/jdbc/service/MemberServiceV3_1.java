package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 * 트랜잭션 동기화 매니져를 사용하여 Thread-Local 기반으로 작동하는 TransactionSynchronizationManager (트랜잭션 동기화 매니저)를 통해 커넥션을 관리한다.
 * 고로 커넥션을 param으로 주고받을 필요가 없어진다.
 * 또한 PlatformTransactionManager이라는 추상화 인터페이스를 통해 DB connection 구현체를 코드 변경 없이 변경 가능하다
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {
//    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) {
        //트랜잭션 시작
        // -> 최초 드랜잭션 시작시 트랜잭션 매니저가 데이터소스를 통해 커넥션을 만들고 트랜잭션을 시작한다.
        // -> 해당 커넥션은 트랜잭션 동기화 매니져에 보관된다.
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            //비즈니스 로직
            bizLogic(fromId, toId, money);
            transactionManager.commit(status); // 커넥션 close
        } catch (Exception e) {
            transactionManager.rollback(status); // 커넥션 close 실패시 롤백
            throw new IllegalStateException(e);
        }
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
