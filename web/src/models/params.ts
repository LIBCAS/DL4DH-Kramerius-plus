import { Sort } from ".";
import { Filter } from "./filter";

export interface Params { 
  disablePagination?: boolean;
  pageOffset?: number;
  pageSize?: number;
  sort?: Sort[]
  filters?: Filter[]
  includeFields?: string[]
}