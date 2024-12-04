package br.ufac.sgcmapi.validator;

import java.time.LocalTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HorarioAtendimentoValidator implements ConstraintValidator<HorarioAtendimento, String> {

    @Override
    public boolean isValid(String hora, ConstraintValidatorContext context) {
        var localTime = LocalTime.parse(hora);
        var limiteInicial = LocalTime.of(14, 0);
        var limiteFinal = LocalTime.of(20, 0);

        var naoEstaAntes = !localTime.isBefore(limiteInicial);
        var naoEstaDepois = !localTime.isAfter(limiteFinal);
        return naoEstaAntes && naoEstaDepois;
    }
    
}
