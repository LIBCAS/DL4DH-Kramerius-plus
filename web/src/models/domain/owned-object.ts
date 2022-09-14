import { DatedObject } from './dated-object'
import { KrameriusUser } from './kramerius-user'

export interface OwnedObject extends DatedObject {
	owner: KrameriusUser
}
