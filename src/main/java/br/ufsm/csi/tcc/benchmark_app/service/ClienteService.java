package br.ufsm.csi.tcc.benchmark_app.service;


import br.ufsm.csi.tcc.benchmark_app.model.Cliente;
import br.ufsm.csi.tcc.benchmark_app.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<Cliente> listar() {
        return clienteRepository.listar();
    }

    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteRepository.buscarPorId(id);
    }

    public Cliente inserir(Cliente cliente) {
        clienteRepository.inserir(cliente);
        return cliente;
    }

    public boolean atualizar(Integer id, Cliente cliente) {
        if (clienteRepository.buscarPorId(id).isEmpty()) {
            return false;
        }

        cliente.setId(id);
        return clienteRepository.atualizar(cliente) > 0;
    }

    public boolean excluir(Integer id) {
        return clienteRepository.excluir(id) > 0;
    }
}