package com.mariano.minhasfinancas.domain.service;

import java.util.List;

public interface ICRUDService<Requisicao, Resposta> {

    List<Resposta> buscarTodos();
    Resposta buscarPorId(Long id);
    Resposta adicionar(Requisicao dto);
    Resposta atualizar(Long id, Requisicao dto);
    void deletar(Long id);
}
