import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MeterReadingDetailComponent } from './meter-reading-detail.component';

describe('MeterReading Management Detail Component', () => {
  let comp: MeterReadingDetailComponent;
  let fixture: ComponentFixture<MeterReadingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MeterReadingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ meterReading: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MeterReadingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MeterReadingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load meterReading on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.meterReading).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
