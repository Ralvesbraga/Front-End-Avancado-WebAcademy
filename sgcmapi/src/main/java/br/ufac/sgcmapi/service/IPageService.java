package br.ufac.sgcmapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPageService<T> {

    public Page<T> get(String termoBusca, Pageable page);
    
}
