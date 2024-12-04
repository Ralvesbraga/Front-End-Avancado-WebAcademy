import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Unidade } from '../../../model/unidade';
import { UnidadeService } from '../../../service/unidade.service';
import { ICrudForm } from '../../i-crud-form';

@Component({
  selector: 'app-unidade-form',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './unidade-form.component.html',
  styles: ``
})
export class UnidadeFormComponent implements ICrudForm<Unidade> {

  constructor(
      private servico: UnidadeService,
      private router: Router,
      private route: ActivatedRoute) { }

  ngOnInit(): void {

    const id = this.route.snapshot.queryParamMap.get('id');
    if (id) {
      this.servico.getById(+id).subscribe({
        next: (resposta: Unidade) => {
          this.registro = resposta;
        }
      });
    }

  }

  registro: Unidade = <Unidade>{};
  
  save(): void {
    this.servico.save(this.registro).subscribe({
      complete: () => {
        alert('Operação realizada com sucesso!');
        this.router.navigate(['/config/unidade-list']);
      }
    });
  }

}
