package com.example.swagger.infrastructure.entities;


import java.math.BigDecimal;

public record Pessoa(String nome, Integer idade, BigDecimal salario, Profissao profissao) {
}
