import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Paciente } from '../../../model/paciente';
import { PacienteService } from '../../../service/paciente.service';
import { ICrudForm } from '../../i-crud-form';

@Component({
  selector: 'app-paciente-form',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './paciente-form.component.html',
  styles: ``
})
export class PacienteFormComponent implements ICrudForm<Paciente> {

  constructor(
      private servico: PacienteService,
      private router: Router,
      private route: ActivatedRoute) { }

  ngOnInit(): void {

    const id = this.route.snapshot.queryParamMap.get('id');
    if (id) {
      this.servico.getById(+id).subscribe({
        next: (resposta: Paciente) => {
          this.registro = resposta;
        }
      });
    }

  }

  registro: Paciente = <Paciente>{};
  
  save(): void {
    this.servico.save(this.registro).subscribe({
      complete: () => {
        alert('Operação realizada com sucesso!');
        this.router.navigate(['/paciente-list']);
      }
    });
  }

}
