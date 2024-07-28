import { Route } from '@angular/router';
import { MainLayoutComponent } from './layout/app-layout/main-layout/main-layout.component';
import { AuthGuard } from '@core/guard/auth.guard';
import { AuthLayoutComponent } from './layout/app-layout/auth-layout/auth-layout.component';
import { Role } from '@core';
import {LandingPageComponent} from "./layout/landing-page/landing-page.component";

export const APP_ROUTE: Route[] = [
  {
    path:   '',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: '/authentication/signin', pathMatch: 'full' },

      {
        path: 'admin',
        canActivate: [AuthGuard],
        data: {
          role: Role.Admin,
        },
        loadChildren: () =>
          import('./admin/admin.routes').then((m) => m.ADMIN_ROUTE),
      },
      {
        path: 'employee',
        canActivate: [AuthGuard],
        data: {
          role: Role.Agent,
        },
        loadChildren: () =>
          import('./agent/agent.routes').then((m) => m.EMPLOYEE_ROUTE),
      },
      {
        path: 'client',
        canActivate: [AuthGuard],
        data: {
          role: Role.Client,
        },
        loadChildren: () =>
          import('./client/client.routes').then((m) => m.CLIENT_ROUTE),
      },
      {
        path: 'apps',
        loadChildren: () =>
          import('./apps/apps.routes').then((m) => m.APPS_ROUTE),
      },
    ],
  },
  {
    path: 'authentication',
    component: AuthLayoutComponent,
    loadChildren: () =>
      import('./authentication/auth.routes').then((m) => m.AUTH_ROUTE),
  },
  {path : 'home' , component : LandingPageComponent}

  //{ path: '**', component: Page404Component },
];
