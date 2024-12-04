package br.ufac.sgcmapi.controller.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.ufac.sgcmapi.controller.dto.ProfissionalDto;
import br.ufac.sgcmapi.model.Profissional;

@Mapper(componentModel = "spring")
public interface ProfissionalMapper {

    @Mapping(target = "especialidade_id", source = "especialidade.id")
    @Mapping(target = "especialidade_nome", source = "especialidade.nome")
    @Mapping(target = "unidade_id", source = "unidade.id")
    @Mapping(target = "unidade_nome", source = "unidade.nome")
    ProfissionalDto toDto(Profissional profissional);

    @InheritInverseConfiguration
    Profissional toEntity(ProfissionalDto dto);

}
