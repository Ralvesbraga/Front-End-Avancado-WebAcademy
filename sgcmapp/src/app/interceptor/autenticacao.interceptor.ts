import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { LoginService } from '../service/login.service';

export const autenticacaoInterceptor: HttpInterceptorFn = (req, next) => {
  const loginService = inject(LoginService);
  req = loginService.getCabecalho(req);
  return next(req);
};
