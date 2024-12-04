package br.ufac.sgcmapi.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.ufac.sgcmapi.model.RespostaErro;

@ControllerAdvice
public class ControllerExcecoes {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespostaErro> validacao(MethodArgumentNotValidException e) {
        var mensagensErro = new ArrayList<String>();
        e.getBindingResult().getAllErrors().forEach(
            erro -> {
                var nomeCampo = ((FieldError) erro).getField();
                var mensagemErro = erro.getDefaultMessage();
                var mensagem = nomeCampo + " " + mensagemErro;
                mensagensErro.add(mensagem);
            }
        );
        var resposta = new RespostaErro(mensagensErro);
        return ResponseEntity.badRequest().body(resposta);
    }
    
}
