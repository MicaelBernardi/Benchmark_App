package br.ufsm.csi.tcc.benchmark_app.repository;


import br.ufsm.csi.tcc.benchmark_app.model.ItemPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemPedidoRepository {

    private final JdbcClient jdbcClient;

    public List<ItemPedido> listarPorPedido(Integer pedidoId) {

        return jdbcClient.sql("""
                        SELECT pedido_id AS pedidoId,
                               produto_id AS produtoId,
                               quantidade,
                               valor_unidade AS valorUnidade
                        FROM item_pedido
                        WHERE pedido_id = :pedidoId
                        ORDER BY produto_id
                        """)
                .param("pedidoId", pedidoId)
                .query(ItemPedido.class)
                .list();
    }

    public int inserir(ItemPedido item) {

        return jdbcClient.sql("""
                        INSERT INTO item_pedido (
                            pedido_id,
                            produto_id,
                            quantidade,
                            valor_unidade
                        )
                        VALUES (
                            :pedidoId,
                            :produtoId,
                            :quantidade,
                            :valorUnidade
                        )
                        """)
                .param("pedidoId", item.getPedidoId())
                .param("produtoId", item.getProdutoId())
                .param("quantidade", item.getQuantidade())
                .param("valorUnidade", item.getValorUnidade())
                .update();
    }

    public int atualizar(ItemPedido item) {

        return jdbcClient.sql("""
                        UPDATE item_pedido
                        SET quantidade = :quantidade,
                            valor_unidade = :valorUnidade
                        WHERE pedido_id = :pedidoId
                          AND produto_id = :produtoId
                        """)
                .param("pedidoId", item.getPedidoId())
                .param("produtoId", item.getProdutoId())
                .param("quantidade", item.getQuantidade())
                .param("valorUnidade", item.getValorUnidade())
                .update();
    }

    public int excluir(Integer pedidoId, Integer produtoId) {

        return jdbcClient.sql("""
                        DELETE FROM item_pedido
                        WHERE pedido_id = :pedidoId
                          AND produto_id = :produtoId
                        """)
                .param("pedidoId", pedidoId)
                .param("produtoId", produtoId)
                .update();
    }
}