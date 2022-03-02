import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMeterReading, getMeterReadingIdentifier } from '../meter-reading.model';

export type EntityResponseType = HttpResponse<IMeterReading>;
export type EntityArrayResponseType = HttpResponse<IMeterReading[]>;

@Injectable({ providedIn: 'root' })
export class MeterReadingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/meter-readings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(meterReading: IMeterReading): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(meterReading);
    return this.http
      .post<IMeterReading>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(meterReading: IMeterReading): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(meterReading);
    return this.http
      .put<IMeterReading>(`${this.resourceUrl}/${getMeterReadingIdentifier(meterReading) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(meterReading: IMeterReading): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(meterReading);
    return this.http
      .patch<IMeterReading>(`${this.resourceUrl}/${getMeterReadingIdentifier(meterReading) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMeterReading>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMeterReading[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMeterReadingToCollectionIfMissing(
    meterReadingCollection: IMeterReading[],
    ...meterReadingsToCheck: (IMeterReading | null | undefined)[]
  ): IMeterReading[] {
    const meterReadings: IMeterReading[] = meterReadingsToCheck.filter(isPresent);
    if (meterReadings.length > 0) {
      const meterReadingCollectionIdentifiers = meterReadingCollection.map(
        meterReadingItem => getMeterReadingIdentifier(meterReadingItem)!
      );
      const meterReadingsToAdd = meterReadings.filter(meterReadingItem => {
        const meterReadingIdentifier = getMeterReadingIdentifier(meterReadingItem);
        if (meterReadingIdentifier == null || meterReadingCollectionIdentifiers.includes(meterReadingIdentifier)) {
          return false;
        }
        meterReadingCollectionIdentifiers.push(meterReadingIdentifier);
        return true;
      });
      return [...meterReadingsToAdd, ...meterReadingCollection];
    }
    return meterReadingCollection;
  }

  protected convertDateFromClient(meterReading: IMeterReading): IMeterReading {
    return Object.assign({}, meterReading, {
      date: meterReading.date?.isValid() ? meterReading.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((meterReading: IMeterReading) => {
        meterReading.date = meterReading.date ? dayjs(meterReading.date) : undefined;
      });
    }
    return res;
  }
}
