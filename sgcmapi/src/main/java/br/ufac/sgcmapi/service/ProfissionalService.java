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

import br.ufac.sgcmapi.model.Profissional;
import br.ufac.sgcmapi.repository.ProfissionalRepository;

@Service
public class ProfissionalService implements ICrudService<Profissional>, IPageService<Profissional> {

    private final ProfissionalRepository repo;

    public ProfissionalService(ProfissionalRepository repo) {
        this.repo = repo;
    }

    @Override
    @Cacheable(
        value = "profissionais",
        key = "'todos'",
        condition = "#termoBusca == null or #termoBusca.isBlank()"
    )
    public List<Profissional> get(String termoBusca) {
        var ordenacao = Sort.by(Sort.Direction.ASC, "nome");
        var page = PageRequest.of(0, Integer.MAX_VALUE, ordenacao);
        var registros = this.get(termoBusca, page).getContent();
        return registros;
    }

    @Override
    @Cacheable(
        value = "profissionais",
        key = "'paginado' + '-page:' + #page.pageNumber + '-size:' + #page.pageSize + '-sort:' + #page.sort.toString()",
        condition = "#termoBusca == null or #termoBusca.isBlank()"
    )
    public Page<Profissional> get(String termoBusca, Pageable page) {
        if (termoBusca != null && !termoBusca.isBlank()) {
            return repo.busca(termoBusca, page);
        }
        return repo.findAll(page);
    }

    @Override
    @Cacheable(value = "profissional", unless = "#result == null")
    public Profissional get(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "profissional", key = "#objeto.id"),
            @CacheEvict(value = "profissionais", allEntries = true)
        }
    )
    public Profissional save(Profissional objeto) {
        return repo.save(objeto);
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "profissional", key = "#id"),
            @CacheEvict(value = "profissionais", allEntries = true)
        }
    )
    public void delete(Long id) {
        repo.deleteById(id);
    }
    
}
