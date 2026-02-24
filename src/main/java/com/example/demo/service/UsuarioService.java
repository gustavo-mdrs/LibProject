package com.example.demo.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void cadastrarUsuario(Usuario usuario) {
        validarNome(usuario.getNome());
        validarCpf(usuario.getCpf());
        validarEmail(usuario.getEmail());
        validarTelefone(usuario.getTelefone());
        validarDataNascimento(usuario.getDataNascimento());
        
        repository.save(usuario);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) throw new IllegalArgumentException("Nome obrigatório");
        if (nome.trim().length() < 3) throw new IllegalArgumentException("Nome deve ter ao menos 3 caracteres");
        if (nome.length() > 255) throw new IllegalArgumentException("Nome excede o limite de 255 caracteres");
        if (nome.matches(".*\\d.*")) throw new IllegalArgumentException("Nome não pode conter números");
    }

    private void validarCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) throw new IllegalArgumentException("CPF obrigatório");
        if (!cpf.matches("\\d{3}-\\d{3}-\\d{3}-\\d{2}")) throw new IllegalArgumentException("Formato de CPF inválido");
        if (repository.existsByCpf(cpf)) throw new IllegalArgumentException("CPF já cadastrado");
    }

    private void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("Email obrigatório");
        if (!email.contains("@")) throw new IllegalArgumentException("Email inválido");
        if (email.length() > 255) throw new IllegalArgumentException("Email excede o limite de 255 caracteres");
        if (repository.existsByEmail(email)) throw new IllegalArgumentException("Email já cadastrado");
    }

    private void validarTelefone(String tel) {
        if (tel == null || tel.trim().isEmpty()) throw new IllegalArgumentException("Telefone obrigatório");
        if (!tel.matches("\\d+")) throw new IllegalArgumentException("Telefone deve conter apenas números");
        if (tel.length() < 10) throw new IllegalArgumentException("Telefone muito curto");
        if (tel.length() > 11) throw new IllegalArgumentException("Telefone muito longo");
    }

    private void validarDataNascimento(LocalDate data) {
        if (data == null) throw new IllegalArgumentException("Data de nascimento obrigatória");
        if (data.isAfter(LocalDate.now())) throw new IllegalArgumentException("Data de nascimento não pode ser futura");
    }
}