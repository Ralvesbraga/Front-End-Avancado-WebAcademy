package br.ufac.sgcmapi.controller.dto;

import java.time.LocalDate;

import br.ufac.sgcmapi.validator.HorarioAtendimento;
import br.ufac.sgcmapi.validator.groups.OnInsert;
import br.ufac.sgcmapi.validator.groups.OnUpdate;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtendimentoDto(
    @NotNull(groups = OnUpdate.class)
    Long id,
    @NotNull(groups = { OnInsert.class, OnUpdate.class })
    @FutureOrPresent(groups = { OnInsert.class, OnUpdate.class })
    LocalDate data,
    @NotBlank(groups = { OnInsert.class, OnUpdate.class })
    @HorarioAtendimento(groups = { OnInsert.class, OnUpdate.class })
    String hora,
    String status,
    @NotNull(groups = { OnInsert.class, OnUpdate.class })
    Long profissional_id,
    String profissional_nome,
    @NotNull(groups = { OnInsert.class, OnUpdate.class })
    Long paciente_id,
    String paciente_nome,
    Long convenio_id,
    String convenio_nome,
    String unidade_nome
) {
    
}
