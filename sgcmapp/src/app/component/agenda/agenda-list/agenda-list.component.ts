import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Atendimento } from '../../../model/atendimento';
import { AtendimentoService } from '../../../service/atendimento.service';
import { BarraComandosComponent } from '../../barra-comandos/barra-comandos.component';
import { ICrudList } from '../../i-crud-list';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-agenda-list',
  standalone: true,
  imports: [CommonModule, BarraComandosComponent, RouterLink],
  templateUrl: './agenda-list.component.html',
  styles: ``
})
export class AgendaListComponent implements ICrudList<Atendimento> {

  constructor(
    private servico: AtendimentoService
  ) {}

  ngOnInit(): void {
    this.get();
  }

  registros: Atendimento[] = [];

  get(termoBusca?: string): void {
    this.servico.get(termoBusca).subscribe({
      next: (resposta: Atendimento[]) => {
        this.registros = resposta
          .filter(item => {
            return ['AGENDADO', 'CONFIRMADO'].includes(item.status)
          })
          .filter(item => {
            let data = new Date().setHours(0, 0, 0, 0);
            let hoje = new Date(data).toISOString().split('T')[0];
            return item.data >= hoje;
          });
      }
    });
  }

  delete(id: number): void {
    if (confirm('Deseja cancelar o agendamento?')) {
      this.servico.delete(id).subscribe({
        complete: () => {
          this.get();
        }
      });
    }
  }

  updateStatus(id: number): void {
    if (confirm('Confirma alteração no status do agendamento?')) {
      this.servico.updateStatus(id).subscribe({
        complete: () => {
          this.get();
        }
      })
    }
  }

}
