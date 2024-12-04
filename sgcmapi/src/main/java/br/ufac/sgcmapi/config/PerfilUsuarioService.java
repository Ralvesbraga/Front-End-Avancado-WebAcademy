package br.ufac.sgcmapi.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.ufac.sgcmapi.service.UsuarioService;

@Service
public class PerfilUsuarioService implements UserDetailsService {

    private final UsuarioService servico;

    public PerfilUsuarioService(UsuarioService servico) {
        this.servico = servico;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = servico.getByNomeUsuario(username);
        return new PerfilUsuario(usuario);
    }
    
}
