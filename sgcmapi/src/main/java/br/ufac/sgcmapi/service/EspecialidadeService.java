package br.ufac.sgcmapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.ufac.sgcmapi.model.Especialidade;
import br.ufac.sgcmapi.repository.EspecialidadeRepository;

@Service
public class EspecialidadeService implements ICrudService<Especialidade>, IPageService<Especialidade> {

    @Autowired
    private EspecialidadeRepository repo;

    @Override
    @Cacheable(
        value = "especialidades",
        key = "'todos'",
        condition = "#termoBusca == null or #termoBusca.isBlank()"
    )
    public List<Especialidade> get(String termoBusca) {
        var ordenacao = Sort.by(Sort.Direction.ASC, "nome");
        var page = PageRequest.of(0, Integer.MAX_VALUE, ordenacao);
        var registros = this.get(termoBusca, page).getContent();
        return registros;
    }

    @Override
    @Cacheable(
        value = "especialidades",
        key = "'paginado' + '-page:' + #page.pageNumber + '-size:' + #page.pageSize + '-sort:' + #page.sort.toString()",
        condition = "#termoBusca == null or #termoBusca.isBlank()"
    )
    public Page<Especialidade> get(String termoBusca, Pageable page) {
        if (termoBusca != null && !termoBusca.isBlank()) {
            return repo.busca(termoBusca, page);
        }
        return repo.findAll(page);
    }

    @Override
    @Cacheable(value = "especialidade", unless = "#result == null")
    public Especialidade get(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "especialidade", key = "#objeto.id"),
            @CacheEvict(value = "especialidades", allEntries = true)
        }
    )
    public Especialidade save(Especialidade objeto) {
       return repo.save(objeto);
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "especialidade", key = "#id"),
            @CacheEvict(value = "especialidades", allEntries = true)
        }
    )
    public void delete(Long id) {
        repo.deleteById(id);
    }
    
}
