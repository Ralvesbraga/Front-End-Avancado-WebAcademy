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

import br.ufac.sgcmapi.model.Unidade;
import br.ufac.sgcmapi.repository.UnidadeRepository;

@Service
public class UnidadeService implements ICrudService<Unidade>, IPageService<Unidade> {

    private final UnidadeRepository repo;

    public UnidadeService(UnidadeRepository repo) {
        this.repo = repo;
    }

    @Override
    @Cacheable(
        value = "unidades",
        key = "'todos'",
        condition = "#termoBusca == null or #termoBusca.isBlank()"
    )
    public List<Unidade> get(String termoBusca) {
        var ordenacao = Sort.by(Sort.Direction.ASC, "nome");
        var page = PageRequest.of(0, Integer.MAX_VALUE, ordenacao);
        var registros = this.get(termoBusca, page).getContent();
        return registros;
    }

    @Override
    @Cacheable(
        value = "unidades",
        key = "'paginado' + '-page:' + #page.pageNumber + '-size:' + #page.pageSize + '-sort:' + #page.sort.toString()",
        condition = "#termoBusca == null or #termoBusca.isBlank()"
    )
    public Page<Unidade> get(String termoBusca, Pageable page) {
        if (termoBusca != null && !termoBusca.isBlank()) {
            return repo.busca(termoBusca, page);
        }
        return repo.findAll(page);
    }

    @Override
    @Cacheable(value = "unidade", unless = "#result == null")
    public Unidade get(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "unidade", key = "#objeto.id"),
            @CacheEvict(value = "unidades", allEntries = true)
        }
    )
    public Unidade save(Unidade objeto) {
        return repo.save(objeto);
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "unidade", key = "#id"),
            @CacheEvict(value = "unidades", allEntries = true)
        }
    )
    public void delete(Long id) {
        repo.deleteById(id);        
    }
    
}
