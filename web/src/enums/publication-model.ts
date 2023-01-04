export const publicationModelList = [
	'MONOGRAPH',
	'MONOGRAPH_UNIT',
	'PERIODICAL',
	'PERIODICAL_VOLUME',
	'PERIODICAL_ITEM',
	'INTERNAL_PART',
	'SUPPLEMENT',
]

export type DigitalObjectModel =
	| 'MONOGRAPH'
	| 'MONOGRAPH_UNIT'
	| 'PERIODICAL'
	| 'PERIODICAL_VOLUME'
	| 'PERIODICAL_ITEM'
	| 'PAGE'
	| 'INTERNAL_PART'
	| 'SUPPLEMENT'

export const DigitalObjectModelMapping: Record<string, string> = {
	MONOGRAPH: 'Monografia',
	MONOGRAPH_UNIT: 'Časť monografie',
	PERIODICAL: 'Periodikum',
	PERIODICAL_VOLUME: 'Ročník periodika',
	PERIODICAL_ITEM: 'Číslo periodika',
	PAGE: 'Stránka',
	INTERNAL_PART: 'Interná časť',
	SUPPLEMENT: 'Príloha',
}
