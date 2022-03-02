import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'meter-reading',
        data: { pageTitle: 'jhipsterApp.meterReading.home.title' },
        loadChildren: () => import('./meter-reading/meter-reading.module').then(m => m.MeterReadingModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
