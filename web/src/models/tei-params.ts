import { Params } from '.'

/**
 * Param options
 * "?" - means "other character"
 */
export type PipeParam = 'n' | 'lemma' | 'pos' | 'msd' | 'join' | '?'
export type TagParam = 'a' | 'g' | 'i' | 'm' | 'n' | 'o' | 'p' | 't' | '?'
export type AltoParam = 'width' | 'height' | 'vpos' | 'hpos' | '?'

export interface TeiParams extends Params {
  udPipeParams?: PipeParam[]
  nameTagParams?: TagParam[];
  altoParams?: AltoParam[]
}