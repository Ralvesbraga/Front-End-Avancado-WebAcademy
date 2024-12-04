package br.ufac.sgcmapi.controller.dto;

import br.ufac.sgcmapi.validator.groups.OnInsert;
import br.ufac.sgcmapi.validator.groups.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ProfissionalDto(
    @NotNull(groups = OnUpdate.class)
    Long id,
    @NotBlank(groups = { OnInsert.class, OnUpdate.class })
    String nome,
    @NotBlank(groups = { OnInsert.class, OnUpdate.class })
    @Email(message = "deve ser do domínio @ufac.br ou @sou.ufac.br", regexp = "^(.+)@(sou\\.)?ufac\\.br$", groups = OnInsert.class)
    @Email(groups = OnUpdate.class)
    String email,
    @NotBlank(groups = { OnInsert.class, OnUpdate.class })
    String registroConselho,
    @NotBlank(groups = { OnInsert.class, OnUpdate.class })
    @Pattern(regexp = "^\\(\\d{2}\\) \\d{4,5}-\\d{4}$", message = "deve seguir o formato (55) 55555-5555", groups = { OnInsert.class, OnUpdate.class })
    String telefone,
    @NotNull(groups = { OnInsert.class, OnUpdate.class })
    Long especialidade_id,
    String especialidade_nome,
    @NotNull(groups = { OnInsert.class, OnUpdate.class })
    Long unidade_id,
    String unidade_nome
) {
    
}
