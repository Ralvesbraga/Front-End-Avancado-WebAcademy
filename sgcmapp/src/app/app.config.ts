import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { HttpClientXsrfModule, provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { erroInterceptor } from './interceptor/erro.interceptor';
import { autenticacaoInterceptor } from './interceptor/autenticacao.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([erroInterceptor, autenticacaoInterceptor])),
    importProvidersFrom(HttpClientXsrfModule)
  ]
};
