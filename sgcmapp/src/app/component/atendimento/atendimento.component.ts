import { Component } from '@angular/core';
import { ICrudList } from '../i-crud-list';
import { Atendimento } from '../../model/atendimento';
import { CommonModule } from '@angular/common';
import { AtendimentoService } from '../../service/atendimento.service';
import { BarraComandosComponent } from '../barra-comandos/barra-comandos.component';

@Component({
  selector: 'app-atendimento',
  standalone: true,
  imports: [CommonModule, BarraComandosComponent],
  templateUrl: './atendimento.component.html',
  styles: ``
})
export class AtendimentoComponent implements ICrudList<Atendimento> {

  constructor(
    private servico: AtendimentoService
  ) { }

  ngOnInit(): void {
    this.get();
  }

  registros: Atendimento[] = [];

  get(termoBusca?: string): void {
    this.servico.get(termoBusca).subscribe({
      next: (reposta: Atendimento[]) => {
        this.registros = reposta
          .filter(item => {
            return ['CHEGADA', 'ATENDIMENTO'].includes(item.status);
          })
          .filter(item => {
            let hoje = new Date().toISOString().split('T')[0];
            return item.data == hoje;
          });
      }
    });
  }

  delete(id: number): void {
    throw new Error('Method not implemented.');
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
