import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Atendimento } from '../../../model/atendimento';
import { Convenio } from '../../../model/convenio';
import { Paciente } from '../../../model/paciente';
import { Profissional } from '../../../model/profissional';
import { AtendimentoService } from '../../../service/atendimento.service';
import { ConvenioService } from '../../../service/convenio.service';
import { PacienteService } from '../../../service/paciente.service';
import { ProfissionalService } from '../../../service/profissional.service';
import { ICrudForm } from '../../i-crud-form';
import { diaUtilValidator } from '../../../validator/dia-util.validator';

@Component({
  selector: 'app-agenda-form',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './agenda-form.component.html',
  styles: ``
})
export class AgendaFormComponent implements ICrudForm<Atendimento> {

  constructor(
    private servico: AtendimentoService,
    private servicoConvenio: ConvenioService,
    private servicoPaciente: PacienteService,
    private servicoProfissional: ProfissionalService,
    private router: Router,
    private rota: ActivatedRoute
  ) {}

  ngOnInit(): void {

    this.servicoConvenio.get().subscribe({
      next: (resposta: Convenio[]) => {
        this.convenios = resposta;
      }
    });

    this.servicoPaciente.get().subscribe({
      next: (resposta: Paciente[]) => {
        this.pacientes = resposta;
      }
    });

    this.servicoProfissional.get().subscribe({
      next: (resposta: Profissional[]) => {
        this.profissionais = resposta;
      }
    });

    const id = this.rota.snapshot.queryParamMap.get('id');
    if (id) {
      this.servico.getById(+id).subscribe({
        next: (resposta: Atendimento) => {
          this.registro = resposta;
          this.formAgenda.patchValue(this.registro);
        }
      });
    }

  }

  registro: Atendimento = <Atendimento>{};
  convenios: Convenio[] = [];
  pacientes: Paciente[] = [];
  profissionais: Profissional[] = [];
  hoje: string = (new Date()).toISOString().split('T')[0];

  formAgenda = new FormGroup({
    data: new FormControl<string | null>(null, diaUtilValidator()),
    hora: new FormControl<string | null>(null),
    profissional_id: new FormControl<number | null>(null),
    paciente_id: new FormControl<number | null>(null),
    convenio_id: new FormControl<number | null>(null),
  });

  get form() {
    return this.formAgenda.controls;
  }

  save(): void {
    this.registro = Object.assign(this.registro, this.formAgenda.value);
    this.servico.save(this.registro).subscribe({
      complete: () => {
        alert('Operação realizada com sucesso!');
        this.router.navigate(['/agenda-list']);
      }
    });
  }

}
