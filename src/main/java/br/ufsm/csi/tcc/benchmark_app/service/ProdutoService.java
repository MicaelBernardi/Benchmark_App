package br.ufsm.csi.tcc.benchmark_app.service;


import br.ufsm.csi.tcc.benchmark_app.model.Produto;
import br.ufsm.csi.tcc.benchmark_app.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public List<Produto> listar() {
        return produtoRepository.listar();
    }

    public Optional<Produto> buscarPorId(Integer id) {
        return produtoRepository.buscarPorId(id);
    }

    public Produto inserir(Produto produto) {
        produtoRepository.inserir(produto);
        return produto;
    }

    public boolean atualizar(Integer id, Produto produto) {
        if (produtoRepository.buscarPorId(id).isEmpty()) {
            return false;
        }

        produto.setId(id);
        return produtoRepository.atualizar(produto) > 0;
    }

    public boolean excluir(Integer id) {
        return produtoRepository.excluir(id) > 0;
    }
}
