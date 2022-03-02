import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMeterReading } from '../meter-reading.model';
import { MeterReadingService } from '../service/meter-reading.service';
import { MeterReadingDeleteDialogComponent } from '../delete/meter-reading-delete-dialog.component';

@Component({
  selector: 'jhi-meter-reading',
  templateUrl: './meter-reading.component.html',
})
export class MeterReadingComponent implements OnInit {
  meterReadings?: IMeterReading[];
  isLoading = false;

  constructor(protected meterReadingService: MeterReadingService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.meterReadingService.query().subscribe({
      next: (res: HttpResponse<IMeterReading[]>) => {
        this.isLoading = false;
        this.meterReadings = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMeterReading): number {
    return item.id!;
  }

  delete(meterReading: IMeterReading): void {
    const modalRef = this.modalService.open(MeterReadingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.meterReading = meterReading;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
