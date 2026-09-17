package br.ufsm.csi.tcc.benchmark_app.repository;


import br.ufsm.csi.tcc.benchmark_app.model.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProdutoRepository {

    private final JdbcClient jdbcClient;

    public List<Produto> listar() {

        return jdbcClient.sql("""
                        SELECT id,
                               nome,
                               preco,
                               qtd_estoque AS qtdEstoque
                        FROM produto
                        ORDER BY id
                        """)
                .query(Produto.class)
                .list();
    }

    public Optional<Produto> buscarPorId(Integer id) {

        return jdbcClient.sql("""
                        SELECT id,
                               nome,
                               preco,
                               qtd_estoque AS qtdEstoque
                        FROM produto
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(Produto.class)
                .optional();
    }

    public int inserir(Produto produto) {

        return jdbcClient.sql("""
                        INSERT INTO produto (
                            nome,
                            preco,
                            qtd_estoque
                        )
                        VALUES (
                            :nome,
                            :preco,
                            :qtdEstoque
                        )
                        """)
                .param("nome", produto.getNome())
                .param("preco", produto.getPreco())
                .param("qtdEstoque", produto.getQtdEstoque())
                .update();
    }

    public int atualizar(Produto produto) {

        return jdbcClient.sql("""
                        UPDATE produto
                        SET nome = :nome,
                            preco = :preco,
                            qtd_estoque = :qtdEstoque
                        WHERE id = :id
                        """)
                .param("id", produto.getId())
                .param("nome", produto.getNome())
                .param("preco", produto.getPreco())
                .param("qtdEstoque", produto.getQtdEstoque())
                .update();
    }

    public int excluir(Integer id) {

        return jdbcClient.sql("""
                        DELETE FROM produto
                        WHERE id = :id
                        """)
                .param("id", id)
                .update();
    }
}
