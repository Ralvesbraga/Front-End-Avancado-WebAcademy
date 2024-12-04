package br.ufac.sgcmapi.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.ufac.sgcmapi.model.Atendimento;
import br.ufac.sgcmapi.model.EStatus;
import br.ufac.sgcmapi.repository.AtendimentoRepository;

@Service
public class AtendimentoService implements ICrudService<Atendimento>, IPageService<Atendimento> {

    private final AtendimentoRepository repo;

    public AtendimentoService(AtendimentoRepository repo) {
        this.repo = repo;
    }

    @Override
    @Cacheable(
        value = "atendimentos",
        key = "'todos'",
        condition = "#termoBusca == null or #termoBusca.isBlank()"
    )
    public List<Atendimento> get(String termoBusca) {
        var ordenacao = Sort.by(Sort.Direction.ASC, "data", "hora");
        var page = PageRequest.of(0, Integer.MAX_VALUE, ordenacao);
        var registros = this.get(termoBusca, page).getContent();
        return registros;
    }

    @Override
    @Cacheable(
        value = "atendimentos",
        key = "'paginado' + '-page:' + #page.pageNumber + '-size:' + #page.pageSize + '-sort:' + #page.sort.toString()",
        condition = "#termoBusca == null or #termoBusca.isBlank()"
    )
    public Page<Atendimento> get(String termoBusca, Pageable page) {
        if (termoBusca != null && !termoBusca.isBlank()) {
            return repo.busca(termoBusca, page);
        }
        return repo.findAll(page);
    }

    @Override
    @Cacheable(value = "atendimento", unless = "#result == null")
    public Atendimento get(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "atendimento", key = "#objeto.id"),
            @CacheEvict(value = "atendimentos", allEntries = true)
        }
    )
    public Atendimento save(Atendimento objeto) {
        return repo.save(objeto);
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "atendimento", key = "#id"),
            @CacheEvict(value = "atendimentos", allEntries = true)
        }
    )
    public void delete(Long id) {
        var registro = this.get(id);
        if (registro != null) {
            registro.setStatus(EStatus.CANCELADO);
            this.save(registro);
        }
    }

    @Caching(
        put = { @CachePut(value = "atendimento", key = "#id") },
        evict = { @CacheEvict(value = "atendimentos", allEntries = true) }
    )   
    public Atendimento updateStatus(Long id) {
        var registro = this.get(id);
        if (registro != null) {
            var novoStatus = registro.getStatus().proximo();
            registro.setStatus(novoStatus);
            this.save(registro);
        }
        return registro;
    }
    
}
