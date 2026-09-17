package br.ufsm.csi.tcc.benchmark_app.service;


import br.ufsm.csi.tcc.benchmark_app.model.ItemPedido;
import br.ufsm.csi.tcc.benchmark_app.model.Pedido;
import br.ufsm.csi.tcc.benchmark_app.repository.ItemPedidoRepository;
import br.ufsm.csi.tcc.benchmark_app.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public List<Pedido> listar() {
        return pedidoRepository.listar();
    }

    public Optional<Pedido> buscarPorId(Integer id) {
        return pedidoRepository.buscarPorId(id);
    }

    public List<ItemPedido> listarItens(Integer pedidoId) {
        return itemPedidoRepository.listarPorPedido(pedidoId);
    }

    public Pedido inserir(Pedido pedido) {
        pedidoRepository.inserir(pedido);
        return pedido;
    }

    public boolean atualizar(Integer id, Pedido pedido) {
        if (pedidoRepository.buscarPorId(id).isEmpty()) {
            return false;
        }

        pedido.setId(id);
        return pedidoRepository.atualizar(pedido) > 0;
    }

    @Transactional
    public boolean excluir(Integer id) {

        if (pedidoRepository.buscarPorId(id).isEmpty()) {
            return false;
        }

        List<ItemPedido> itens = itemPedidoRepository.listarPorPedido(id);

        for (ItemPedido item : itens) {
            itemPedidoRepository.excluir(item.getPedidoId(), item.getProdutoId());
        }

        return pedidoRepository.excluir(id) > 0;
    }

    public boolean inserirItem(Integer pedidoId, ItemPedido item) {

        if (pedidoRepository.buscarPorId(pedidoId).isEmpty()) {
            return false;
        }

        item.setPedidoId(pedidoId);

        return itemPedidoRepository.inserir(item) > 0;
    }

    public boolean atualizarItem(Integer pedidoId, ItemPedido item) {

        item.setPedidoId(pedidoId);

        return itemPedidoRepository.atualizar(item) > 0;
    }

    public boolean excluirItem(Integer pedidoId, Integer produtoId) {
        return itemPedidoRepository.excluir(pedidoId, produtoId) > 0;
    }
}
