import { DatedObject } from 'models/domain/dated-object'
import { User } from 'models/domain/user'

export interface Request extends DatedObject {
	name: string
	publicationIds: string[]
	owner: User
}
