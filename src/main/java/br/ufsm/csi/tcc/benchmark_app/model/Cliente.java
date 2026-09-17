package br.ufsm.csi.tcc.benchmark_app.model;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    private Integer id;
    private String nome;
    private BigDecimal saldo;
}
