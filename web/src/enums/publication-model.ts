export const publicationModelList = [
	'monograph',
	'monographunit',
	'periodical',
	'periodicalvolume',
	'periodicalitem',
	'internalpart',
	'supplement',
]

export type DigitalObjectModel =
	| 'monograph'
	| 'monographunit'
	| 'periodical'
	| 'periodicalvolume'
	| 'periodicalitem'
	| 'page'
	| 'internalpart'
	| 'supplement'

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
