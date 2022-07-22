package br.com.javafy.service;


import br.com.javafy.entity.UsuarioEntity;
import br.com.javafy.exceptions.PessoaNaoCadastradaException;
import br.com.javafy.dto.UsuarioDTO;
import br.com.javafy.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeguidoresService {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    public UsuarioDTO converterParaUsuarioDTO (UsuarioEntity user){
        return objectMapper.convertValue(user, UsuarioDTO.class);
    }


    public UsuarioEntity converterDTOParaUsuario (UsuarioDTO user){
        return objectMapper.convertValue(user, UsuarioEntity.class);
    }

    public List<UsuarioDTO> getAllSeguidores(Integer idUser) throws PessoaNaoCadastradaException {

        Optional<UsuarioEntity> usuarioOptional = findById(idUser);

        if(usuarioOptional.isEmpty()){
            throw new PessoaNaoCadastradaException("Usuario não encontrado");
        }
        UsuarioEntity usuario = usuarioOptional.get();


        return usuarioOptional.get()
                .getSeguidores()
                .stream()
                .map(this::converterParaUsuarioDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioDTO> getAllSeguindo(Integer idUser) throws PessoaNaoCadastradaException {

        Optional<UsuarioEntity> usuarioOptional = findById(idUser);

       if(usuarioOptional.isEmpty()){
           throw new PessoaNaoCadastradaException("Usuario não encontrado");
       }
       UsuarioEntity usuario = usuarioOptional.get();


       return usuarioOptional.get()
               .getSeguindo()
                .stream()
                .map(this::converterParaUsuarioDTO)
                .collect(Collectors.toList());

    }

    public boolean seguirUser(Integer meuId,Integer idSeguindo) throws PessoaNaoCadastradaException {

        Optional<UsuarioEntity> optionalUsuario = findById(meuId);
        Optional<UsuarioEntity> usuarioParaSeguir = findById(idSeguindo);
        UsuarioEntity usuarioEntity = optionalUsuario.get();
        usuarioEntity.getSeguidores().add(usuarioParaSeguir.get());
        repository.save(usuarioEntity);
        return true;

    }

    public void deixarDeSeguirUsuario(Integer meuId, Integer idSeguindo) throws PessoaNaoCadastradaException {

        Optional<UsuarioEntity> optionalUsuario = findById(meuId);
        Optional<UsuarioEntity> usuarioParaSeguir = findById(idSeguindo);
        UsuarioEntity usuarioEntity = optionalUsuario.get();
        usuarioEntity.getSeguidores().remove(usuarioParaSeguir.get());
        usuarioEntity.setIdUsuario(meuId);
        repository.save(usuarioEntity);

    }

    public Optional<UsuarioEntity> findById (Integer idUsuario) throws PessoaNaoCadastradaException {
       return Optional.ofNullable(repository.findById(idUsuario)
               .orElseThrow(() -> new PessoaNaoCadastradaException("Pessoa não cadastrada ou não existe")));
    }
}
