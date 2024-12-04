import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Especialidade } from '../../../model/especialidade';
import { EspecialidadeService } from '../../../service/especialidade.service';
import { ICrudForm } from '../../i-crud-form';

@Component({
  selector: 'app-especialidade-form',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './especialidade-form.component.html',
  styles: ``
})
export class EspecialidadeFormComponent implements ICrudForm<Especialidade> {

  constructor(
      private servico: EspecialidadeService,
      private router: Router,
      private route: ActivatedRoute) { }

  ngOnInit(): void {

    const id = this.route.snapshot.queryParamMap.get('id');
    if (id) {
      this.servico.getById(+id).subscribe({
        next: (resposta: Especialidade) => {
          this.registro = resposta;
        }
      });
    }

  }

  registro: Especialidade = <Especialidade>{};
  
  save(): void {
    this.servico.save(this.registro).subscribe({
      complete: () => {
        alert('Operação realizada com sucesso!');
        this.router.navigate(['/config/especialidade-list']);
      }
    });
  }

}
