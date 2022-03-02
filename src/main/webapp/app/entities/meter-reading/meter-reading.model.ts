import dayjs from 'dayjs/esm';

export interface IMeterReading {
  id?: number;
  date?: dayjs.Dayjs | null;
  electricityConsumption?: number | null;
}

export class MeterReading implements IMeterReading {
  constructor(public id?: number, public date?: dayjs.Dayjs | null, public electricityConsumption?: number | null) {}
}

export function getMeterReadingIdentifier(meterReading: IMeterReading): number | undefined {
  return meterReading.id;
}
