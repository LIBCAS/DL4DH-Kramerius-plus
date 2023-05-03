import { DatedObject } from 'models/domain/dated-object'
import { User } from 'models/domain/user'

export interface Request extends DatedObject {
	name: string
	publicationIds: string[]
	owner: User
	state: RequestState
}

export type RequestState =
	| 'CREATED'
	| 'RUNNING'
	| 'FAILED'
	| 'COMPLETED'
	| 'PARTIAL'
	| 'CANCELLED'

export const RequestStateMapping = {
	CREATED: 'Vytvořená',
	RUNNING: 'Běží',
	FAILED: 'Selhala',
	COMPLETED: 'Dokončená',
	PARTIAL: 'Částečně selhala',
	CANCELLED: 'Zrušena',
}
