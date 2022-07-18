package br.com.javafy.service;

import br.com.javafy.dto.ComentarioDTO;
import br.com.javafy.entity.Comentario;
import br.com.javafy.exceptions.PessoaNaoCadastradaException;
import br.com.javafy.repository.ComentariosRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    @Autowired
    ComentariosRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    public ComentarioDTO converterComentario(Comentario comentario) {
        return objectMapper.convertValue(comentario, ComentarioDTO.class);
    }

    public Comentario converterComentarioDTO(ComentarioDTO comentarioDTO) {
        return objectMapper.convertValue(comentarioDTO, Comentario.class);
    }

    public List<ComentarioDTO> list() throws SQLException {
        return repository
                .list().stream()
                .map(this::converterComentario)
                .collect(Collectors.toList());
    }

    public ComentarioDTO create(Integer idUser, Integer idPlaylist, ComentarioDTO comentarioDTO) throws SQLException {
        Comentario comentarioEntity = converterComentarioDTO(comentarioDTO);
        repository.create(idUser,idPlaylist,comentarioEntity);
        return converterComentario(comentarioEntity);
    }

    public ComentarioDTO update(ComentarioDTO comentarioDTO, Integer idComentario)
            throws PessoaNaoCadastradaException, SQLException {

        Comentario comentario = converterComentarioDTO(findById(idComentario));
        System.out.println("comentario = "+comentario);
        boolean comentarioAtualizado = repository.update(idComentario, comentario);
        if(comentarioAtualizado){
            comentario.setIdComentario(idComentario);
        } else {
            throw new PessoaNaoCadastradaException("ID informado é inválido.");
        }

        return converterComentario(comentario);
    }

    public void delete(Integer idComentario) throws PessoaNaoCadastradaException, SQLException {
        ComentarioDTO comentarioRecuperado = findById(idComentario);
        Comentario comentarioEntity = converterComentarioDTO(comentarioRecuperado);
        System.out.println("comentarioEntity "+comentarioEntity);
        repository.delete(idComentario);
    }

    public ComentarioDTO findById(Integer id) throws PessoaNaoCadastradaException, SQLException {
        return converterComentario(repository.findByID(id));
    }

}
