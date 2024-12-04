package br.ufac.sgcmapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.ufac.sgcmapi.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @Query("""
        SELECT p FROM Paciente p WHERE p.nome LIKE %:termoBusca%
        OR p.email LIKE %:termoBusca%
        OR p.telefone LIKE %:termoBusca%
        OR p.grupoSanguineo LIKE %:termoBusca%
        OR p.sexo LIKE %:termoBusca%
        OR p.cep LIKE %:termoBusca%
        OR p.endereco LIKE %:termoBusca%
    """)
    Page<Paciente> busca(String termoBusca, Pageable page);
    
}
