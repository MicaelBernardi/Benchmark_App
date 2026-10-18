package br.ufsm.csi.tcc.benchmark_app.repository;

import br.ufsm.csi.tcc.benchmark_app.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PedidoRepository {

    private final JdbcClient jdbcClient;

    public List<Pedido> listar() {

        return jdbcClient.sql("""
                        SELECT id,
                               cliente_id AS clienteId,
                               data,
                               status,
                               valor_total AS valorTotal
                        FROM pedido
                        ORDER BY id
                        """)
                .query(Pedido.class)
                .list();
    }

    public Optional<Pedido> buscarPorId(Integer id) {

        return jdbcClient.sql("""
                        SELECT id,
                               cliente_id AS clienteId,
                               data,
                               status,
                               valor_total AS valorTotal
                        FROM pedido
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(Pedido.class)
                .optional();
    }

    public Integer inserir(Pedido pedido) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("""
                        INSERT INTO pedido (
                            cliente_id,
                            valor_total
                        )
                        VALUES (
                            :clienteId,
                            :valorTotal
                        )
                        """)
                .param("clienteId", pedido.getClienteId())
                .param("valorTotal", pedido.getValorTotal())
                .update(keyHolder, "id"); // A captura do ID gerado ocorre aqui

        return keyHolder.getKeyAs(Integer.class);
    }
    public int atualizar(Pedido pedido) {

        return jdbcClient.sql("""
                        UPDATE pedido
                        SET cliente_id = :clienteId,
                            data = :data,
                            status = :status,
                            valor_total = :valorTotal
                        WHERE id = :id
                        """)
                .param("id", pedido.getId())
                .param("clienteId", pedido.getClienteId())
                .param("data", pedido.getData())
                .param("status", pedido.getStatus())
                .param("valorTotal", pedido.getValorTotal())
                .update();
    }

    public int excluir(Integer id) {

        return jdbcClient.sql("""
                        DELETE FROM pedido
                        WHERE id = :id
                        """)
                .param("id", id)
                .update();
    }
}
