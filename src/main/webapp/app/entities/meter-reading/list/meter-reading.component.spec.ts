import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MeterReadingService } from '../service/meter-reading.service';

import { MeterReadingComponent } from './meter-reading.component';

describe('MeterReading Management Component', () => {
  let comp: MeterReadingComponent;
  let fixture: ComponentFixture<MeterReadingComponent>;
  let service: MeterReadingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MeterReadingComponent],
    })
      .overrideTemplate(MeterReadingComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MeterReadingComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MeterReadingService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.meterReadings?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
