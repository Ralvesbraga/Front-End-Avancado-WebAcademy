package br.ufac.sgcmapi.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufac.sgcmapi.controller.dto.AtendimentoDto;
import br.ufac.sgcmapi.controller.mapper.AtendimentoMapper;
import br.ufac.sgcmapi.model.RespostaErro;
import br.ufac.sgcmapi.service.AtendimentoService;
import br.ufac.sgcmapi.validator.groups.OnInsert;
import br.ufac.sgcmapi.validator.groups.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/atendimento")
@Tag(name = "Atendimento", description = "Endpoints para gerenciar atendimentos")
public class AtendimentoController implements ICrudController<AtendimentoDto>, IPageController<AtendimentoDto> {

    private final AtendimentoService servico;
    private final AtendimentoMapper mapper;

    public AtendimentoController(
            AtendimentoService servico,
            AtendimentoMapper mapper) {
        this.servico = servico;
        this.mapper = mapper;
    }

    @Override
    @GetMapping(value = "/consultar/todos", produces = "application/json")
    @Operation(
        summary = "Obter todos os atendimentos ou filtrar por termo de busca (sem paginação)",
        description = "Obtém uma lista não paginada de todos os atendimentos cadastrados no sistema ou que contenham o termo de busca informado."
    )
    public ResponseEntity<List<AtendimentoDto>> get(@RequestParam(required = false) String termoBusca) {
        var registros = servico.get(termoBusca);
        var dtos = registros.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    @GetMapping(value = "/consultar", produces = "application/json")
    @Operation(
        summary = "Obter todos os atendimentos ou filtrar por termo de busca (com paginação)",
        description = "Obtém uma lista paginada de todos os atendimentos cadastrados no sistema ou que contenham o termo de busca informado."
    )
    public ResponseEntity<Page<AtendimentoDto>> get(
            @RequestParam(required = false) String termoBusca,
            @SortDefaults({
                @SortDefault(sort = "data", direction = Sort.Direction.ASC),
                @SortDefault(sort = "hora", direction = Sort.Direction.ASC)
            })
            @ParameterObject
            Pageable page) {
        var registros = servico.get(termoBusca, page);
        var dtos = registros.map(mapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @Override
    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(
        summary = "Obter um atendimento",
        description = "Obtém um atendimento cadastrado no sistema baseado no ID informado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Atendimento encontrado"),
        @ApiResponse(responseCode = "404", description = "Atendimento não encontrado", content = @Content())
    })
    public ResponseEntity<AtendimentoDto> get(@PathVariable Long id) {
        var registro = servico.get(id);
        if (registro == null) {
            return ResponseEntity.notFound().build();
        }
        var dto = mapper.toDto(registro);
        return ResponseEntity.ok(dto);
    }

    @Override
    @PostMapping(value = "/inserir", produces = "application/json")
    @Operation(
        summary = "Cadastrar um atendimento",
        description = "Cadastra um novo atendimento no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Atendimento cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = RespostaErro.class)))
    })
    public ResponseEntity<AtendimentoDto> insert(@RequestBody @Validated(OnInsert.class) AtendimentoDto objeto) {
        var objetoConvertido = mapper.toEntity(objeto);
        var registro = servico.save(objetoConvertido);
        var dto = mapper.toDto(registro);
        return ResponseEntity.created(null).body(dto);
    }

    @Override
    @PutMapping(value = "/atualizar", produces = "application/json")
    @Operation(
        summary = "Atualizar um atendimento",
        description = "Atualiza um atendimento cadastrado no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Atendimento atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = RespostaErro.class)))
    })
    public ResponseEntity<AtendimentoDto> update(@RequestBody @Validated(OnUpdate.class) AtendimentoDto objeto) {
        var objetoConvertido = mapper.toEntity(objeto);
        var registro = servico.save(objetoConvertido);
        var dto = mapper.toDto(registro);
        return ResponseEntity.ok(dto);
    }

    @Override
    @DeleteMapping("/remover/{id}")
    @Operation(
        summary = "Cancela um agendamento",
        description = "Altera o status de um agendamento para cancelado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agendamento cancelado com sucesso", content = @Content())
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servico.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/status/{id}", produces = "application/json")
    @Operation(
        summary = "Atualiza o status de um atendimento",
        description = "Altera o status para o próximo valor de acordo com o fluxo de agendamento/atendimento: AGENDADO > CONFIRMADO > CHEGADA > ATENDIMENTO > ENCERRADO"
    )
    public ResponseEntity<AtendimentoDto> updateStatus(@PathVariable Long id) {
        var registro = servico.updateStatus(id);
        var dto = mapper.toDto(registro);
        return ResponseEntity.ok(dto);
    }
    
}
