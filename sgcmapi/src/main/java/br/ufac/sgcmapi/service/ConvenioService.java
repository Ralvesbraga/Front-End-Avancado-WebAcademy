package br.ufac.sgcmapi.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.ufac.sgcmapi.model.Convenio;
import br.ufac.sgcmapi.repository.ConvenioRepository;

@Service
public class ConvenioService implements ICrudService<Convenio>, IPageService<Convenio> {

    private final ConvenioRepository repo;

    public ConvenioService(ConvenioRepository repo) {
        this.repo = repo;
    }

    @Override
    @Cacheable(
        value = "convenios",
        key = "'todos'",
        condition = "#termoBusca == null or #termoBusca.isBlank()"
    )
    public List<Convenio> get(String termoBusca) {
        var ordenacao = Sort.by(Sort.Direction.ASC, "nome");
        var page = PageRequest.of(0, Integer.MAX_VALUE, ordenacao);
        var registros = this.get(termoBusca, page).getContent();
        return registros;
    }

    @Override
    @Cacheable(
        value = "convenios",
        key = "'paginado' + '-page:' + #page.pageNumber + '-size:' + #page.pageSize + '-sort:' + #page.sort.toString()",
        condition = "#termoBusca == null or #termoBusca.isBlank()"
    )
    public Page<Convenio> get(String termoBusca, Pageable page) {
        if (termoBusca != null && !termoBusca.isBlank()) {
            return repo.busca(termoBusca, page);
        }
        return repo.findAll(page);
    }

    @Override
    @Cacheable(value = "convenio", unless = "#result == null")
    public Convenio get(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "convenio", key = "#objeto.id"),
            @CacheEvict(value = "convenios", allEntries = true)
        }
    )
    public Convenio save(Convenio objeto) {
        return repo.save(objeto);
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "convenio", key = "#id"),
            @CacheEvict(value = "convenios", allEntries = true)
        }
    )
    public void delete(Long id) {
        repo.deleteById(id);
    }
    
}
