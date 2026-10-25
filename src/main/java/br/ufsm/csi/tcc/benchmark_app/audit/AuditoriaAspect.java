package br.ufsm.csi.tcc.benchmark_app.audit;

import br.ufsm.csi.tcc.benchmark_app.dto.PedidoRequest;
import br.ufsm.csi.tcc.benchmark_app.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.auditoria.enabled", havingValue = "true")
public class AuditoriaAspect {

    private static final String USUARIO = "benchmark_app";

    private final JdbcClient jdbcClient;

    @AfterReturning(
            pointcut = "execution(* br.ufsm.csi.tcc.benchmark_app.service.PedidoService.criarCompleto(..))",
            returning = "pedido")
    public void auditarCriacaoPedido(JoinPoint jp, Pedido pedido) {
        PedidoRequest req = (PedidoRequest) jp.getArgs()[0];

        String itens = req.itens().stream()
                .map(i -> i.produtoId() + ":" + i.quantidade())
                .collect(Collectors.joining(","));

        jdbcClient.sql("""
                        INSERT INTO auditoria_aplicacao
                            (operacao, horario, usuario, pedido_id, cliente_id, valor_total, itens)
                        VALUES
                            (:operacao, :horario, :usuario, :pedidoId, :clienteId, :valorTotal, :itens)
                        """)
                .param("operacao", "CRIAR_PEDIDO")
                .param("horario", LocalDateTime.now())
                .param("usuario", USUARIO)
                .param("pedidoId", pedido.getId())
                .param("clienteId", pedido.getClienteId())
                .param("valorTotal", pedido.getValorTotal())
                .param("itens", itens)
                .update();
    }
}