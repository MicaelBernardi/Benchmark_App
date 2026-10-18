package br.ufsm.csi.tcc.benchmark_app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoDetalhe(
        Integer pedidoId,
        Integer clienteId,
        LocalDateTime data,
        String status,
        BigDecimal valorTotal,
        Integer produtoId,
        String produtoNome,
        Integer quantidade,
        BigDecimal valorUnidade) {
}
