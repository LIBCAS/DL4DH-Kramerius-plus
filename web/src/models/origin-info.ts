import { DateIssued } from ".";
import { Place } from "./place";

export interface OriginInfo { 
  publisher?: string;
  dateIssued?: DateIssued[];
  places?: Place[]
}