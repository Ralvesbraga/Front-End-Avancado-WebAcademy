import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Atendimento } from '../model/atendimento';
import { ICrudService } from './i-crud-service';

@Injectable({
  providedIn: 'root'
})
export class AtendimentoService implements ICrudService<Atendimento> {

  constructor(
    private http: HttpClient
  ) { }

  apiUrl: string = environment.API_URL + '/atendimento';

  get(termoBusca?: string): Observable<Atendimento[]> {
    let url = this.apiUrl + '/consultar/todos';
    if (termoBusca) {
      url += '?termoBusca=' + termoBusca;
    }
    return this.http.get<Atendimento[]>(url);
  }

  getById(id: number): Observable<Atendimento> {
    let url = this.apiUrl + '/' + id;
    return this.http.get<Atendimento>(url);
  }

  save(objeto: Atendimento): Observable<Atendimento> {
    let url = this.apiUrl;
    if (objeto.id) {
      url += '/atualizar';
      return this.http.put<Atendimento>(url, objeto);
    } else {
      url += '/inserir';
      return this.http.post<Atendimento>(url, objeto);
    }
  }

  delete(id: number): Observable<void> {
    let url = this.apiUrl + '/remover/' + id;
    return this.http.delete<void>(url);
  }

  updateStatus(id: number): Observable<Atendimento> {
    let url = this.apiUrl + '/status/' + id;
    return this.http.put<Atendimento>(url, null);
  }

}
