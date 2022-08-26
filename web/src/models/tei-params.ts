/**
 * Param options
 * "?" - means "other character"
 */
export type UDPipeParam = 'n' | 'lemma' | 'pos' | 'msd' | 'join' | '?'
export type NameTagParam = 'a' | 'g' | 'i' | 'm' | 'n' | 'o' | 'p' | 't' | '?'
export type AltoParam = 'width' | 'height' | 'vpos' | 'hpos' | '?'

export interface TeiExportParams {
	udPipeParams?: UDPipeParam[]
	nameTagParams?: NameTagParam[]
	altoParams?: AltoParam[]
}
