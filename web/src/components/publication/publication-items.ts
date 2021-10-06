import { Sort } from "../../models";
import { AltoParam, PipeParam, TagParam } from "../../models/tei-params";

export const fieldOptions = [
  {
    id: "title",
    label: "Nadpis",
  },
  {
    id: "index",
    label: "Číslo strany",
  },
  {
    id: "nameTagMetadata",
    label: "NameTag - úroveň stránky",
  },
  {
    id: "token.tokenIndex",
    label: "Obsah tokenu",
  },
  {
    id: "token.linguisticMetadata.position",
    label: "Číslo tokenu vo vete",
  },
  {
    id: "token.linguisticMetadata.lemma",
    label: "Lemma",
  },
  {
    id: "token.linguisticMetadata.uPosTag",
    label: "UPosTag",
  },
  {
    id: "token.linguisticMetadata.xPosTag",
    label: "XPosTag",
  },
  {
    id: "token.linguisticMetadata.feats",
    label: "Feats",
  },
  {
    id: "token.nameTagMetadata",
    label: "NameTag - úroveň tokenu",
  },
];


export const sortOptions:Sort[]= [
  {
    field: 'index',
    direction: 'ASC'
  },
  {
    field: 'index',
    direction: 'DESC'
  }
]

export const udPipeParamsOptions:PipeParam[] = [
  'n',
  'lemma',
  'pos',
  'msd',
  'join',
  '?'
]

export const nameTagParamsOptions: TagParam[] = [
  'a',
  'g',
  'i',
  'm',
  'n',
  'o',
  'p',
  't'
]

export const altoParamsOptions: AltoParam[] = [
  'height',
  'width',
  'vpos',
  'hpos',
  '?'
]