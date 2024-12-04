import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Usuario } from '../../../model/usuario';
import { UsuarioService } from '../../../service/usuario.service';
import { ICrudForm } from '../../i-crud-form';

@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './usuario-form.component.html',
  styles: ``
})
export class UsuarioFormComponent implements ICrudForm<Usuario> {

  constructor(
      private servico: UsuarioService,
      private router: Router,
      private route: ActivatedRoute) { }

  ngOnInit(): void {

    const id = this.route.snapshot.queryParamMap.get('id');
    if (id) {
      this.servico.getById(+id).subscribe({
        next: (resposta: Usuario) => {
          this.registro = resposta;
        }
      });
    }

  }

  registro: Usuario = <Usuario>{};
  
  save(): void {
    this.servico.save(this.registro).subscribe({
      complete: () => {
        alert('Operação realizada com sucesso!');
        this.router.navigate(['/config/usuario-list']);
      }
    });
  }

}
