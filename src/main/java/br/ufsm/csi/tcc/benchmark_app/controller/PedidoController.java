package br.ufsm.csi.tcc.benchmark_app.controller;


import br.ufsm.csi.tcc.benchmark_app.dto.PedidoDetalhe;
import br.ufsm.csi.tcc.benchmark_app.dto.PedidoRequest;
import br.ufsm.csi.tcc.benchmark_app.model.ItemPedido;
import br.ufsm.csi.tcc.benchmark_app.model.Pedido;
import br.ufsm.csi.tcc.benchmark_app.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Integer id) {

        return pedidoService.buscarPorId(id)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemPedido>> listarItens(@PathVariable Integer id) {

        if (pedidoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pedidoService.listarItens(id));
    }

    @PostMapping
    public ResponseEntity<Pedido> inserir(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.inserir(pedido));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Integer id, @RequestBody Pedido pedido) {

        if (!pedidoService.atualizar(id, pedido)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {

        if (!pedidoService.excluir(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<Void> inserirItem(@PathVariable Integer id, @RequestBody ItemPedido item) {

        if (!pedidoService.inserirItem(id, item)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{pedidoId}/itens/{produtoId}")
    public ResponseEntity<Void> atualizarItem(
            @PathVariable Integer pedidoId, @PathVariable Integer produtoId, @RequestBody ItemPedido item) {

        item.setProdutoId(produtoId);

        if (!pedidoService.atualizarItem(pedidoId, item)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{pedidoId}/itens/{produtoId}")
    public ResponseEntity<Void> excluirItem(@PathVariable Integer pedidoId, @PathVariable Integer produtoId) {

        if (!pedidoService.excluirItem(pedidoId, produtoId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    // ---- Endpoints usados nos benchmarks ----

    // Escrita: Pedido + Item_Pedido + UPDATE de estoque
    @PostMapping("/completo")
    public ResponseEntity<Pedido> inserirCompleto(@RequestBody PedidoRequest request) {
        return ResponseEntity.ok(pedidoService.criarCompleto(request));
    }

    // Leitura: join Pedido + Item_Pedido + Produto
    @GetMapping("/{id}/detalhes")
    public ResponseEntity<List<PedidoDetalhe>> detalhes(@PathVariable Integer id) {

        List<PedidoDetalhe> detalhes = pedidoService.buscarDetalhe(id);

        if (detalhes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(detalhes);
    }
}