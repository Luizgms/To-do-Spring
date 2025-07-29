package com.luizgms.todosimple.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper; // <--- NOVA IMPORTAÇÃO NECESSÁRIA!
import com.fasterxml.jackson.annotation.JsonInclude; // <--- Opcional, mas útil!

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Adicione esta anotação para não serializar campos nulos
public class ErrorResponse {
    private final int status;
    private final String message;
    private String stackTrace;
    private List<ValidationError> errors;

    private String path;
    private Long timestamp;

    // Construtor já está ok com @RequiredArgsConstructor para campos final

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class ValidationError {
        private final String field;
        private final String message;
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new ValidationError(field, message));
    }

    // --- MÉTODO toJson() MELHORADO COM JACKSON ---
    public String toJson() {
        try {
            // ObjectMapper é do Jackson e converte o objeto Java para String JSON
            // Ele vai serializar todos os getters da sua classe
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            // Em caso de erro na serialização, retorne um JSON de erro genérico
            // e logue o erro para depuração
            // log.error("Erro ao serializar ErrorResponse para JSON", e); // Se puder usar
            // o log aqui
            return "{\"status\":500,\"message\":\"Error serializing error response\"}";
        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    // E no construtor de ErrorResponse para inicializar o timestamp:
}