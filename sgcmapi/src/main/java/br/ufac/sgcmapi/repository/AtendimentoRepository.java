package br.ufac.sgcmapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.ufac.sgcmapi.model.Atendimento;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    @Query("""
        SELECT a FROM Atendimento a
        LEFT JOIN Profissional p ON p = a.profissional
        LEFT JOIN Paciente pa ON pa = a.paciente
        LEFT JOIN Convenio c ON c = a.convenio
        LEFT JOIN Especialidade e ON e = p.especialidade
        LEFT JOIN Unidade u ON u = p.unidade
        WHERE p.nome LIKE %:termoBusca%
        OR pa.nome LIKE %:termoBusca%
        OR c.nome LIKE %:termoBusca%
        OR e.nome LIKE %:termoBusca%
        OR u.nome LIKE %:termoBusca%
    """)
    Page<Atendimento> busca(String termoBusca, Pageable page);
    
}
