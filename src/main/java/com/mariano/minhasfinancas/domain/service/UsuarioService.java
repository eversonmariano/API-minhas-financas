package com.mariano.minhasfinancas.domain.service;

import com.mariano.minhasfinancas.domain.exception.ResourceBadRequestException;
import com.mariano.minhasfinancas.domain.exception.ResourceNotFoundException;
import com.mariano.minhasfinancas.domain.model.Usuario;
import com.mariano.minhasfinancas.domain.repository.UsuarioRepository;
import com.mariano.minhasfinancas.dto.usuario.UsuarioRequestDTO;
import com.mariano.minhasfinancas.dto.usuario.UsuarioResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class UsuarioService implements ICRUDService<UsuarioRequestDTO, UsuarioResponseDTO>{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioResponseDTO> buscarTodos() {

        List<Usuario> usuarios = usuarioRepository.findAll();

        // List<UsuarioResponseDto> usuariosDto = new ArrayList<>();

        // for (Usuario usuario : usuarios) {
        // UsuarioResponseDto dto = mapper.map(usuario, UsuarioResponseDto.class);
        // usuariosDto.add(dto);
        // }

        // return usuariosDto;

        return usuarios.stream()
                .map(usuario -> mapper.map(usuario, UsuarioResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO buscarPorId(Long id) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if (optUsuario.isEmpty()) {
            throw new ResourceNotFoundException("Não foi possível encontrar o usuário com o id: " + id);
        }

        return mapper.map(optUsuario.get(), UsuarioResponseDTO.class);
    }


    @Override
    public UsuarioResponseDTO adicionar(UsuarioRequestDTO dto) {

        validarUsuario(dto);

        Optional<Usuario> optUsuario = usuarioRepository.findByEmail(dto.getEmail());

        if(optUsuario.isPresent()){
            throw new ResourceBadRequestException("Já existe um usuário cadastro com o e-mail: " + dto.getEmail());
        }

        Usuario usuario = mapper.map(dto, Usuario.class);

        String senha = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senha);

        usuario.setDataCadastro(new Date());
        usuario = usuarioRepository.save(usuario);
        return mapper.map(usuario, UsuarioResponseDTO.class);
    }

    @Override
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {

        UsuarioResponseDTO usuarioBanco = buscarPorId(id);
        validarUsuario(dto);

        Usuario usuario = mapper.map(dto, Usuario.class);

        String senha = passwordEncoder.encode(dto.getSenha());
        usuario.setSenha(senha);

        usuario.setId(id);
        usuario.setDataInativacao(usuarioBanco.getDataInativacao());
        usuario.setDataCadastro(usuarioBanco.getDataCadastro());

        usuario = usuarioRepository.save(usuario);

        return mapper.map(usuario, UsuarioResponseDTO.class);
    }

    @Override
    public void deletar(Long id) {

        //Encontro usuário
       UsuarioResponseDTO usuarioEncontrado = buscarPorId(id);
        //transformo ele num usuario de banco(model)
       Usuario usuario = mapper.map(usuarioEncontrado, Usuario.class);
       //seto uma data de inativação
       usuario.setDataInativacao(new Date());
       //salvo no repository
       usuarioRepository.save(usuario);
    }

    private void validarUsuario(UsuarioRequestDTO dto) {

        if (dto.getEmail() == null || dto.getSenha() == null) {
            throw new ResourceBadRequestException("E-mail e senha são obrigatórios");
        }
    }
}