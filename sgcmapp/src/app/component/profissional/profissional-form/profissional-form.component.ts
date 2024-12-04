import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Especialidade } from '../../../model/especialidade';
import { Profissional } from '../../../model/profissional';
import { Unidade } from '../../../model/unidade';
import { EspecialidadeService } from '../../../service/especialidade.service';
import { ProfissionalService } from '../../../service/profissional.service';
import { UnidadeService } from '../../../service/unidade.service';
import { ICrudForm } from '../../i-crud-form';

@Component({
  selector: 'app-profissional-form',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './profissional-form.component.html',
  styles: ``
})
export class ProfissionalFormComponent implements ICrudForm<Profissional> {

  constructor(
    private servico: ProfissionalService,
    private servicoEspecialidade: EspecialidadeService,
    private servicoUnidade: UnidadeService,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit(): void {

    this.servicoEspecialidade.get().subscribe({
      next: (resposta: Especialidade[]) => {
        this.especialidades = resposta;
      }
    });

    this.servicoUnidade.get().subscribe({
      next: (resposta: Unidade[]) => {
        this.unidades = resposta;
      }
    });

    const id = this.route.snapshot.queryParamMap.get('id');
    if (id) {
      this.servico.getById(+id).subscribe({
        next: (resposta: Profissional) => {
          this.registro = resposta;
        }
      });
    }

  }

  registro: Profissional = <Profissional>{};
  especialidades: Especialidade[] = [];
  unidades: Unidade[] = [];
  
  save(): void {
    this.servico.save(this.registro).subscribe({
      complete: () => {
        alert('Operação realizada com sucesso!');
        this.router.navigate(['/profissional-list']);
      }
    });
  }

}
