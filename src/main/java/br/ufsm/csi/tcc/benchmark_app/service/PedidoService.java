package br.ufsm.csi.tcc.benchmark_app.service;


import br.ufsm.csi.tcc.benchmark_app.dto.PedidoDetalhe;
import br.ufsm.csi.tcc.benchmark_app.dto.PedidoRequest;
import br.ufsm.csi.tcc.benchmark_app.model.ItemPedido;
import br.ufsm.csi.tcc.benchmark_app.model.Pedido;
import br.ufsm.csi.tcc.benchmark_app.repository.ItemPedidoRepository;
import br.ufsm.csi.tcc.benchmark_app.repository.PedidoRepository;
import br.ufsm.csi.tcc.benchmark_app.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoRepository produtoRepository;

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
        Integer idGerado = pedidoRepository.inserir(pedido);
        pedido.setId(idGerado);

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

    @Transactional
    public Pedido criarCompleto(PedidoRequest req) {

        List<PedidoRequest.ItemRequest> itens = req.itens().stream()
                .sorted(Comparator.comparing(PedidoRequest.ItemRequest::produtoId))
                .toList();

        BigDecimal total = BigDecimal.ZERO;
        List<ItemPedido> itensPedido = new java.util.ArrayList<>();

        for (PedidoRequest.ItemRequest it : itens) {
            BigDecimal preco = produtoRepository.baixarEstoque(it.produtoId(), it.quantidade())
                    .orElseThrow(() -> new IllegalStateException(
                            "Produto inexistente: " + it.produtoId()));

            total = total.add(preco.multiply(BigDecimal.valueOf(it.quantidade())));
            itensPedido.add(ItemPedido.builder()
                    .produtoId(it.produtoId())
                    .quantidade(it.quantidade())
                    .valorUnidade(preco)
                    .build());
        }

        Pedido pedido = Pedido.builder()
                .clienteId(req.clienteId())
                .valorTotal(total)
                .build();
        pedido.setId(pedidoRepository.inserir(pedido));

        for (ItemPedido item : itensPedido) {
            item.setPedidoId(pedido.getId());
            itemPedidoRepository.inserir(item);
        }

        return pedido;
    }

    public List<PedidoDetalhe> buscarDetalhe(Integer id) {
        return pedidoRepository.buscarDetalhe(id);
    }
}