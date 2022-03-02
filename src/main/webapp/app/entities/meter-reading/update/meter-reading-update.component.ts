import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMeterReading, MeterReading } from '../meter-reading.model';
import { MeterReadingService } from '../service/meter-reading.service';

@Component({
  selector: 'jhi-meter-reading-update',
  templateUrl: './meter-reading-update.component.html',
})
export class MeterReadingUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    date: [],
    electricityConsumption: [],
  });

  constructor(protected meterReadingService: MeterReadingService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ meterReading }) => {
      this.updateForm(meterReading);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const meterReading = this.createFromForm();
    if (meterReading.id !== undefined) {
      this.subscribeToSaveResponse(this.meterReadingService.update(meterReading));
    } else {
      this.subscribeToSaveResponse(this.meterReadingService.create(meterReading));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeterReading>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(meterReading: IMeterReading): void {
    this.editForm.patchValue({
      id: meterReading.id,
      date: meterReading.date,
      electricityConsumption: meterReading.electricityConsumption,
    });
  }

  protected createFromForm(): IMeterReading {
    return {
      ...new MeterReading(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      electricityConsumption: this.editForm.get(['electricityConsumption'])!.value,
    };
  }
}
