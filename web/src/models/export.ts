import { FileRef } from "./fileRef";

export interface Export {
  id?: string;
  publicationId?: string;
  publicationTitle?: string;
  created?: string;
  deleted?: string;
  fileRef?: FileRef;
}
