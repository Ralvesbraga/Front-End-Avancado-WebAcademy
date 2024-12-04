package br.ufac.sgcmapi.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.ufac.sgcmapi.controller.dto.UsuarioDto;
import br.ufac.sgcmapi.model.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDto toDto(Usuario usuario);

    @Mapping(target = "senha", ignore = true)
    Usuario toEntity(UsuarioDto dto);
    
}
