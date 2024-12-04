package br.ufac.sgcmapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IPageController<T> {

    public ResponseEntity<Page<T>> get(String termoBusca, Pageable page);
    
}
