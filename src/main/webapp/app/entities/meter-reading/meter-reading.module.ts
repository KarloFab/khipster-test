import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MeterReadingComponent } from './list/meter-reading.component';
import { MeterReadingDetailComponent } from './detail/meter-reading-detail.component';
import { MeterReadingUpdateComponent } from './update/meter-reading-update.component';
import { MeterReadingDeleteDialogComponent } from './delete/meter-reading-delete-dialog.component';
import { MeterReadingRoutingModule } from './route/meter-reading-routing.module';

@NgModule({
  imports: [SharedModule, MeterReadingRoutingModule],
  declarations: [MeterReadingComponent, MeterReadingDetailComponent, MeterReadingUpdateComponent, MeterReadingDeleteDialogComponent],
  entryComponents: [MeterReadingDeleteDialogComponent],
})
export class MeterReadingModule {}
