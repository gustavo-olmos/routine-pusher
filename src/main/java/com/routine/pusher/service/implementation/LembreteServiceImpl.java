package com.routine.pusher.service.implementation;

import com.routine.pusher.mapper.LembreteMapper;
import com.routine.pusher.model.dto.LembreteDTO;
import com.routine.pusher.repository.LembreteRepository;
import com.routine.pusher.service.interfaces.LembreteService;
import com.routine.pusher.util.SortInfo;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LembreteServiceImpl implements LembreteService
{
    private final Logger LOGGER = LoggerFactory.getLogger( LembreteServiceImpl.class );

    private LembreteRepository repository;
    private LembreteMapper mapper;

    public LembreteServiceImpl( LembreteRepository repository, LembreteMapper mapper )
    {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public LembreteDTO adicionar( LembreteDTO dto )
    {
        LOGGER.debug("Adicionando lembrete");

        return Stream.of( dto )
                .map( mapper::toEntity )
                .peek( lembrete -> {
                    lembrete.setDataCriacao( LocalDateTime.now( ) );
                    lembrete.getTarefas( ).forEach( tarefa -> tarefa.setLembrete( lembrete ) );
                } )
                .map( repository::save )
                .map( mapper::toDto )
                .toList( ).get( 0 );
    }

    @Override
    public List<LembreteDTO> listar( String atributo, boolean ordemReversa )
    {
        LOGGER.debug("Listando lembretes por: {}", atributo);

        List<LembreteDTO> lembretes = new ArrayList<>(
                repository.findAll( )
                        .stream( )
                        .map( mapper::toDto )
                        .collect( Collectors.toList( ) ) );

        lembretes.sort( new SortInfo<LembreteDTO>( atributo, ordemReversa ) );

        return lembretes;
    }

    @Override
    public LembreteDTO atualizar( Long id, LembreteDTO dto )
    {
        LOGGER.debug("Alterando lembrete");

        return repository.findById( id )
                .map( entidade -> {
                    mapper.atualizaEntidade( dto, entidade );
                    entidade.getTarefas( ).forEach( tarefa -> tarefa.setLembrete( entidade ) );
                    repository.save( entidade );
                    return mapper.toDto( entidade );
                } )
                .orElseThrow( ( ) ->
                        new EntityNotFoundException( "Lembrete não encontrada para o id: " + id ) );
    }

    @Override
    public boolean excluir( Long id )
    {
        LOGGER.debug("Excluindo lembrete");

        if( repository.existsById( id ) ) {
            repository.deleteById( id );
            return true;
        }
        return false;
    }
}
