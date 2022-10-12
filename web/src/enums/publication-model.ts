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
	monograph: 'Monografia',
	monographunit: 'Časť monografie',
	periodical: 'Periodikum',
	periodicalvolume: 'Ročník periodika',
	periodicalitem: 'Číslo periodika',
	page: 'Stránka',
	internalpart: 'Interná časť',
	supplement: 'Príloha',
}
