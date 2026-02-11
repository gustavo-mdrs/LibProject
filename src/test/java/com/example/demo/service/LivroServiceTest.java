package com.example.demo.service;

import com.example.demo.model.Livro;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LivroServiceTest {

    private LivroService livroService = new LivroService();

    @Test
    void naoDevePermitirCadastroComTituloVazio() {

        Livro livro = new Livro(
                "", 
                "1234567890123",
                "Autor Teste",
                2020,
                "Editora Teste",
                1,
                100
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            livroService.cadastrar(livro);
        });

        assertEquals("Título é obrigatório", exception.getMessage());
    }


    @Test
    void naoDevePermitirCadastroComIsbnVazio() {

        Livro livro = new Livro(
                "Livro Teste",
                "",
                "Autor Teste",
                2020,
                "Editora Teste",
                1,
                100
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            livroService.cadastrar(livro);
        });

        assertEquals("ISBN é obrigatório", exception.getMessage());
    }

    

}