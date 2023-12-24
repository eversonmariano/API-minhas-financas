package com.mariano.minhasfinancas.domain.service;

import java.util.List;

public interface ICRUDService<Requisicao, Resposta> {

    List<Resposta> lerTodos();
    Resposta lerPorId(Long id);
    Resposta adicionar(Requisicao dto);
    Resposta atualizar(Requisicao dto);
    void deletar(Long id);
}
