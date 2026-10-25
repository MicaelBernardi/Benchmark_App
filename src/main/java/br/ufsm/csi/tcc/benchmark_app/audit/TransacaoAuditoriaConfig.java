package br.ufsm.csi.tcc.benchmark_app.audit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true, order = Ordered.LOWEST_PRECEDENCE - 1)
@ConditionalOnProperty(name = "app.auditoria.enabled", havingValue = "true")
public class TransacaoAuditoriaConfig {
}