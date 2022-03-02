import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MeterReadingComponent } from '../list/meter-reading.component';
import { MeterReadingDetailComponent } from '../detail/meter-reading-detail.component';
import { MeterReadingUpdateComponent } from '../update/meter-reading-update.component';
import { MeterReadingRoutingResolveService } from './meter-reading-routing-resolve.service';

const meterReadingRoute: Routes = [
  {
    path: '',
    component: MeterReadingComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MeterReadingDetailComponent,
    resolve: {
      meterReading: MeterReadingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MeterReadingUpdateComponent,
    resolve: {
      meterReading: MeterReadingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MeterReadingUpdateComponent,
    resolve: {
      meterReading: MeterReadingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(meterReadingRoute)],
  exports: [RouterModule],
})
export class MeterReadingRoutingModule {}
