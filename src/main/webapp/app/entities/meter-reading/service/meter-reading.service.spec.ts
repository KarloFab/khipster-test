import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMeterReading, MeterReading } from '../meter-reading.model';

import { MeterReadingService } from './meter-reading.service';

describe('MeterReading Service', () => {
  let service: MeterReadingService;
  let httpMock: HttpTestingController;
  let elemDefault: IMeterReading;
  let expectedResult: IMeterReading | IMeterReading[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MeterReadingService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      date: currentDate,
      electricityConsumption: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a MeterReading', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new MeterReading()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MeterReading', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_FORMAT),
          electricityConsumption: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MeterReading', () => {
      const patchObject = Object.assign(
        {
          electricityConsumption: 1,
        },
        new MeterReading()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MeterReading', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_FORMAT),
          electricityConsumption: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a MeterReading', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMeterReadingToCollectionIfMissing', () => {
      it('should add a MeterReading to an empty array', () => {
        const meterReading: IMeterReading = { id: 123 };
        expectedResult = service.addMeterReadingToCollectionIfMissing([], meterReading);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(meterReading);
      });

      it('should not add a MeterReading to an array that contains it', () => {
        const meterReading: IMeterReading = { id: 123 };
        const meterReadingCollection: IMeterReading[] = [
          {
            ...meterReading,
          },
          { id: 456 },
        ];
        expectedResult = service.addMeterReadingToCollectionIfMissing(meterReadingCollection, meterReading);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MeterReading to an array that doesn't contain it", () => {
        const meterReading: IMeterReading = { id: 123 };
        const meterReadingCollection: IMeterReading[] = [{ id: 456 }];
        expectedResult = service.addMeterReadingToCollectionIfMissing(meterReadingCollection, meterReading);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(meterReading);
      });

      it('should add only unique MeterReading to an array', () => {
        const meterReadingArray: IMeterReading[] = [{ id: 123 }, { id: 456 }, { id: 93761 }];
        const meterReadingCollection: IMeterReading[] = [{ id: 123 }];
        expectedResult = service.addMeterReadingToCollectionIfMissing(meterReadingCollection, ...meterReadingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const meterReading: IMeterReading = { id: 123 };
        const meterReading2: IMeterReading = { id: 456 };
        expectedResult = service.addMeterReadingToCollectionIfMissing([], meterReading, meterReading2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(meterReading);
        expect(expectedResult).toContain(meterReading2);
      });

      it('should accept null and undefined values', () => {
        const meterReading: IMeterReading = { id: 123 };
        expectedResult = service.addMeterReadingToCollectionIfMissing([], null, meterReading, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(meterReading);
      });

      it('should return initial array if no MeterReading is added', () => {
        const meterReadingCollection: IMeterReading[] = [{ id: 123 }];
        expectedResult = service.addMeterReadingToCollectionIfMissing(meterReadingCollection, undefined, null);
        expect(expectedResult).toEqual(meterReadingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
