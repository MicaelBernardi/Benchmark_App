package br.ufsm.csi.tcc.benchmark_app.dto;

import java.util.List;

public record PedidoRequest(Integer clienteId, List<ItemRequest> itens) {

    public record ItemRequest(Integer produtoId, Integer quantidade) {
    }
}
