package br.ufsm.csi.tcc.benchmark_app.repository;


import br.ufsm.csi.tcc.benchmark_app.model.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClienteRepository {

    private final JdbcClient jdbcClient;

    public List<Cliente> listar() {

        return jdbcClient.sql("""
                        SELECT id,
                               nome,
                               saldo
                        FROM cliente
                        ORDER BY id
                        """)
                .query(Cliente.class)
                .list();
    }

    public Optional<Cliente> buscarPorId(Integer id) {

        return jdbcClient.sql("""
                        SELECT id,
                               nome,
                               saldo
                        FROM cliente
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(Cliente.class)
                .optional();
    }

    public int inserir(Cliente cliente) {

        return jdbcClient.sql("""
                        INSERT INTO cliente (
                            nome,
                            saldo
                        )
                        VALUES (
                            :nome,
                            :saldo
                        )
                        """)
                .param("nome", cliente.getNome())
                .param("saldo", cliente.getSaldo())
                .update();
    }

    public int atualizar(Cliente cliente) {

        return jdbcClient.sql("""
                        UPDATE cliente
                        SET nome = :nome,
                            saldo = :saldo
                        WHERE id = :id
                        """)
                .param("id", cliente.getId())
                .param("nome", cliente.getNome())
                .param("saldo", cliente.getSaldo())
                .update();
    }

    public int excluir(Integer id) {

        return jdbcClient.sql("""
                        DELETE FROM cliente
                        WHERE id = :id
                        """)
                .param("id", id)
                .update();
    }
}
