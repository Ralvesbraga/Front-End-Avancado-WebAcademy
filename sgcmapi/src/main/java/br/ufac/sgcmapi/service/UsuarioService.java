package br.ufac.sgcmapi.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.ufac.sgcmapi.model.Usuario;
import br.ufac.sgcmapi.repository.UsuarioRepository;

@Service
public class UsuarioService implements ICrudService<Usuario>, IPageService<Usuario> {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    @Cacheable(
        value = "usuarios",
        key = "'todos'",
        condition = "#termoBusca == null or #termoBusca.isBlank()"
    )
    public List<Usuario> get(String termoBusca) {
        var ordenacao = Sort.by(Sort.Direction.ASC, "nomeCompleto");
        var page = PageRequest.of(0, Integer.MAX_VALUE, ordenacao);
        var registros = this.get(termoBusca, page).getContent();
        return registros;
    }

    @Override
    @Cacheable(
        value = "usuarios",
        key = "'paginado' + '-page:' + #page.pageNumber + '-size:' + #page.pageSize + '-sort:' + #page.sort.toString()",
        condition = "#termoBusca == null or #termoBusca.isBlank()"
    )
    public Page<Usuario> get(String termoBusca, Pageable page) {
        if (termoBusca != null && !termoBusca.isBlank()) {
            return repo.busca(termoBusca, page);
        }
        return repo.findAll(page);
    }

    @Override
    @Cacheable(value = "usuario", unless = "#result == null")
    public Usuario get(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "usuario", key = "#objeto.id"),
            @CacheEvict(value = "usuarios", allEntries = true)
        }
    )
    public Usuario save(Usuario objeto) {
        if (objeto.getSenha() == null || objeto.getSenha().isBlank()) {
            var usuario = get(objeto.getId());
            if (usuario != null) {
                objeto.setSenha(usuario.getSenha());
            }
        } else {
            var passEncoder = new BCryptPasswordEncoder();
            var senhaCriptografada = passEncoder.encode(objeto.getSenha());
            objeto.setSenha(senhaCriptografada);
        }
        return repo.save(objeto);
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "usuario", key = "#id"),
            @CacheEvict(value = "usuarios", allEntries = true)
        }
    )
    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Usuario getByNomeUsuario(String nomeUsuario) {
        return repo.findByNomeUsuario(nomeUsuario);
    }
    
}
