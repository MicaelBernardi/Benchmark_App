package br.ufsm.csi.tcc.benchmark_app.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    private Integer id;
    private Integer clienteId;
    private LocalDateTime data;
    private String status;
    private BigDecimal valorTotal;
}
