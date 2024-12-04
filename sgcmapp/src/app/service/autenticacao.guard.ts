import { CanActivateFn } from '@angular/router';
import { LoginService } from './login.service';
import { inject } from '@angular/core';

export const autenticacaoGuard: CanActivateFn = (route, state) => {
  const servicoLogin = inject(LoginService);
  if (!servicoLogin.estaLogado()) {
    servicoLogin.logout();
    return false;
  }
  const papelExigido = route.data['papel'];
  const papelUsuario = servicoLogin.usuarioAutenticado.value.papel;
  const podeAcessar = papelExigido == papelUsuario ||!papelExigido;
  return podeAcessar;
};
