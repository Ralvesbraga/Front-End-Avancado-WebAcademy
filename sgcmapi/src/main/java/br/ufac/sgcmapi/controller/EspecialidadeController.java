package br.ufac.sgcmapi.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufac.sgcmapi.model.Especialidade;
import br.ufac.sgcmapi.model.RespostaErro;
import br.ufac.sgcmapi.service.EspecialidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/config/especialidade")
@Tag(name = "Especialidade", description = "Endpoints para gerenciar especialidades")
public class EspecialidadeController implements ICrudController<Especialidade>, IPageController<Especialidade> {

    private final EspecialidadeService servico;

    public EspecialidadeController(EspecialidadeService servico) {
        this.servico = servico;
    }

    @Override
    @GetMapping(value = "/consultar/todos", produces = "application/json")
    @Operation(
        summary = "Obter todos os registros ou filtrar por termo de busca (sem paginação)",
        description = "Obtém uma lista não paginada de todos os registros cadastrados no sistema ou que contenham o termo de busca informado."
    )
    public ResponseEntity<List<Especialidade>> get(@RequestParam(required = false) String termoBusca) {
        var registros = servico.get(termoBusca);
        return ResponseEntity.ok(registros);
    }

    @Override
    @GetMapping(value = "/consultar", produces = "application/json")
    @Operation(
        summary = "Obter todos os registros ou filtrar por termo de busca (com paginação)",
        description = "Obtém uma lista paginada de todos os registros cadastrados no sistema ou que contenham o termo de busca informado."
    )
    public ResponseEntity<Page<Especialidade>> get(
            @RequestParam(required = false) String termoBusca,
            @SortDefaults({
                @SortDefault(sort = "nome", direction = Sort.Direction.ASC)
            })
            @ParameterObject
            Pageable page) {
        var registros = servico.get(termoBusca, page);
        return ResponseEntity.ok(registros);
    }

    @Override
    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(
        summary = "Obter um registro",
        description = "Obtém um registro cadastrado no sistema baseado no ID informado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro encontrado"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado", content = @Content())
    })
    public ResponseEntity<Especialidade> get(@PathVariable Long id) {
        var registro = servico.get(id);
        if (registro == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(registro);
    }

    @Override
    @PostMapping(value = "/inserir", produces = "application/json")
    @Operation(
        summary = "Inserir um novo registro",
        description = "Insere um novo registro no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registro cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = RespostaErro.class)))
    })
    public ResponseEntity<Especialidade> insert(@RequestBody Especialidade objeto) {
        var registro = servico.save(objeto);
        return ResponseEntity.created(null).body(registro);
    }

    @Override
    @PutMapping(value = "/atualizar", produces = "application/json")
    @Operation(
        summary = "Atualizar um registro",
        description = "Atualiza um registro cadastrado no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = RespostaErro.class)))
    })
    public ResponseEntity<Especialidade> update(@RequestBody Especialidade objeto) {
        var registro = servico.save(objeto);
        return ResponseEntity.ok(registro);
    }

    @Override
    @DeleteMapping("/remover/{id}")
    @Operation(
        summary = "Remove um registro",
        description = "Remove um registro pelo ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro removido com sucesso", content = @Content())
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servico.delete(id);
        return ResponseEntity.ok().build();
    }
    
}
