package br.ufac.sgcmapi.controller.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.ufac.sgcmapi.controller.dto.AtendimentoDto;
import br.ufac.sgcmapi.model.Atendimento;
import br.ufac.sgcmapi.model.Convenio;

@Mapper(componentModel = "spring")
public interface AtendimentoMapper {

    @Mapping(target = "profissional_id", source = "profissional.id")
    @Mapping(target = "profissional_nome", source = "profissional.nome")
    @Mapping(target = "convenio_id", source = "convenio.id")
    @Mapping(target = "convenio_nome", source = "convenio.nome")
    @Mapping(target = "paciente_id", source = "paciente.id")
    @Mapping(target = "paciente_nome", source = "paciente.nome")
    @Mapping(target = "unidade_nome", source = "profissional.unidade.nome")
    AtendimentoDto toDto(Atendimento atendimento);

    @InheritInverseConfiguration
    @Mapping(target = "convenio", expression = "java(mapConvenio(dto.convenio_id()))")
    Atendimento toEntity(AtendimentoDto dto);

    default Convenio mapConvenio(Long convenioId) {
        if (convenioId == null) {
            return null;
        }
        var convenio = new Convenio();
        convenio.setId(convenioId);
        return convenio;
    }
    
}
