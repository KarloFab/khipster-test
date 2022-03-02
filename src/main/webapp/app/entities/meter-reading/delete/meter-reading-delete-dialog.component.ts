import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMeterReading } from '../meter-reading.model';
import { MeterReadingService } from '../service/meter-reading.service';

@Component({
  templateUrl: './meter-reading-delete-dialog.component.html',
})
export class MeterReadingDeleteDialogComponent {
  meterReading?: IMeterReading;

  constructor(protected meterReadingService: MeterReadingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.meterReadingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
