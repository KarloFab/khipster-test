import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MeterReadingService } from '../service/meter-reading.service';
import { IMeterReading, MeterReading } from '../meter-reading.model';

import { MeterReadingUpdateComponent } from './meter-reading-update.component';

describe('MeterReading Management Update Component', () => {
  let comp: MeterReadingUpdateComponent;
  let fixture: ComponentFixture<MeterReadingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let meterReadingService: MeterReadingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MeterReadingUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MeterReadingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MeterReadingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    meterReadingService = TestBed.inject(MeterReadingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const meterReading: IMeterReading = { id: 456 };

      activatedRoute.data = of({ meterReading });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(meterReading));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MeterReading>>();
      const meterReading = { id: 123 };
      jest.spyOn(meterReadingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meterReading });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: meterReading }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(meterReadingService.update).toHaveBeenCalledWith(meterReading);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MeterReading>>();
      const meterReading = new MeterReading();
      jest.spyOn(meterReadingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meterReading });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: meterReading }));
      saveSubject.complete();

      // THEN
      expect(meterReadingService.create).toHaveBeenCalledWith(meterReading);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MeterReading>>();
      const meterReading = { id: 123 };
      jest.spyOn(meterReadingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meterReading });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(meterReadingService.update).toHaveBeenCalledWith(meterReading);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
