import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { HttpClientXsrfModule, provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { autenticacaoInterceptor } from './interceptor/autenticacao.interceptor';
import { erroInterceptor } from './interceptor/erro.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([erroInterceptor, autenticacaoInterceptor])),
    importProvidersFrom(HttpClientXsrfModule)
  ]
};
