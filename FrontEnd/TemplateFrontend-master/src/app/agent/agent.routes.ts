import { Page404Component } from '../authentication/page404/page404.component';
import { Route } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AttendancesComponent } from './attendance/attendance.component';
import { MyTeamsComponent } from './myteam/myteam.component';
import { SettingsComponent } from './settings/settings.component';
import { MyProjectsComponent } from './my-projects/my-projects.component';

import { ChatComponent } from './chat/chat.component';
export const EMPLOYEE_ROUTE: Route[] = [
  {
    path: 'dashboard',
    component: DashboardComponent,
  },
  {
    path: 'attendance',
    component: AttendancesComponent,
  },
  {
    path: 'myteam',
    component: MyTeamsComponent,
  },
  {
    path: 'myprojects',
    component: MyProjectsComponent,
  },
  {
    path: 'chat',
    component: ChatComponent,
  },
  {
    path: 'settings',
    component: SettingsComponent,
  },
  { path: '**', component: Page404Component },
];

