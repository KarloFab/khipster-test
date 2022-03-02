import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMeterReading, MeterReading } from '../meter-reading.model';
import { MeterReadingService } from '../service/meter-reading.service';

@Injectable({ providedIn: 'root' })
export class MeterReadingRoutingResolveService implements Resolve<IMeterReading> {
  constructor(protected service: MeterReadingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMeterReading> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((meterReading: HttpResponse<MeterReading>) => {
          if (meterReading.body) {
            return of(meterReading.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MeterReading());
  }
}
