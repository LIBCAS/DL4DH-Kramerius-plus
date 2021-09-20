import { Identifier } from "./identifier";
import { Name } from "./name";
import { OriginInfo } from "./originInfo";
import { PhysicalDescription } from "./physicalDescription";
import { TitleInfo } from "./titleInfo";

export interface ModsMetadata {
  titleInfos?: TitleInfo[];
  name?: Name;
  genre?: string;
  physicalDescription?: PhysicalDescription;
  originInfo?: OriginInfo;
  identifiers?: Identifier[]
}