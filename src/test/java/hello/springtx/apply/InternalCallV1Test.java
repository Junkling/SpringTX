package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 내부 호출의 경우 proxy를 호출할 수 없다
 */

@Slf4j
@SpringBootTest
public class InternalCallV1Test {

    @Autowired
    CallService callService;

    @Test
    void printProxy() {
        log.info("callService class={}", callService.getClass());
    }
    @Test
    void internalCall() {
        callService.internal();
    }
    @Test
    void externalCall() {
        callService.external();
    }

    @TestConfiguration
    static class InternalCallC1TestConfig {
        @Bean
        CallService callService() {
            return new CallService();
        }
    }


    @Slf4j
    static class CallService {
        public void external() {
            log.info("call external");
            printTxInfo();
            internal();
        }


        @Transactional
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
            boolean currentTransactionReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly={}", currentTransactionReadOnly);
        }
    }
}
