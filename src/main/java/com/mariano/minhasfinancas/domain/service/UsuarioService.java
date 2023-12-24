package com.mariano.minhasfinancas.domain.service;

import com.mariano.minhasfinancas.domain.model.Usuario;
import com.mariano.minhasfinancas.domain.repository.UsuarioRepository;
import com.mariano.minhasfinancas.dto.usuario.UsuarioRequestDTO;
import com.mariano.minhasfinancas.dto.usuario.UsuarioResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsuarioService implements ICRUDService<UsuarioRequestDTO, UsuarioResponseDTO>{

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public List<UsuarioResponseDTO> lerTodos() {
        //O que o bd me deu
        List<Usuario> usuarios = usuarioRepository.findAll();
//        //o que irei devolver, através de uma conversão de um usuario normal para um usuarioDTO
//        List<UsuarioResponseDTO> usuariosResponseDTO = new ArrayList<>();
//        //abaixo como funcionará a conversao...
//        for (Usuario usuario : usuarios) {
//            var dto = mapper.map(usuario, UsuarioResponseDTO.class );
//            usuariosResponseDTO.add(dto);
//        }
//        return usuariosResponseDTO;
        return usuarios.stream()
                .map(usuario -> mapper.map(usuario, UsuarioResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO lerPorId(Long id) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if (optUsuario.isEmpty()){
            //Aqui lançar exception...
        }
        return mapper.map(optUsuario.get(), UsuarioResponseDTO.class);
    }

    @Override
    public UsuarioResponseDTO adicionar(UsuarioRequestDTO dto) {
        return null;
    }

    @Override
    public UsuarioResponseDTO atualizar(UsuarioRequestDTO dto) {
        return null;
    }

    @Override
    public void deletar(Long id) {

    }
}
