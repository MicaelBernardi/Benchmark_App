package br.ufsm.csi.tcc.benchmark_app.model;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

    private Integer pedidoId;
    private Integer produtoId;
    private Integer quantidade;
    private BigDecimal valorUnidade;
}
