import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMeterReading } from '../meter-reading.model';

@Component({
  selector: 'jhi-meter-reading-detail',
  templateUrl: './meter-reading-detail.component.html',
})
export class MeterReadingDetailComponent implements OnInit {
  meterReading: IMeterReading | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ meterReading }) => {
      this.meterReading = meterReading;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
