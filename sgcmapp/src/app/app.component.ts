import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterOutlet } from '@angular/router';
import { LoginService } from './service/login.service';
import { Usuario } from './model/usuario';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  title = 'SGCM';
  paginaAtual = '';
  usuario: Usuario = <Usuario>{};

  constructor(
      private router: Router,
      private loginService: LoginService) {

    router.events.subscribe(evento => {
      if (evento instanceof NavigationEnd) {
        this.paginaAtual = evento.url;
      }
    });

    loginService.usuarioAutenticado.subscribe({
      next: (usuario: Usuario) => {
        this.usuario = usuario;
      }
    });

  }

  isAdmin(): boolean {
    return this.usuario.papel == 'ROLE_ADMIN';
  }

  estaLogado(): boolean {
    return this.loginService.estaLogado();
  }

  logout(): void {
    this.loginService.logout();
  }

}
