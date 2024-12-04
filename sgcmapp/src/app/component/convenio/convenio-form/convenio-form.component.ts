import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Convenio } from '../../../model/convenio';
import { ConvenioService } from '../../../service/convenio.service';
import { ICrudForm } from '../../i-crud-form';

@Component({
  selector: 'app-convenio-form',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './convenio-form.component.html',
  styles: ``
})
export class ConvenioFormComponent implements ICrudForm<Convenio> {

  constructor(
      private servico: ConvenioService,
      private router: Router,
      private route: ActivatedRoute) { }

  ngOnInit(): void {

    const id = this.route.snapshot.queryParamMap.get('id');
    if (id) {
      this.servico.getById(+id).subscribe({
        next: (resposta: Convenio) => {
          this.registro = resposta;
        }
      });
    }

    this.registro.ativo = this.registro.ativo || false;

  }

  registro: Convenio = <Convenio>{};
  
  save(): void {
    this.servico.save(this.registro).subscribe({
      complete: () => {
        alert('Operação realizada com sucesso!');
        this.router.navigate(['/convenio-list']);
      }
    });
  }

}
