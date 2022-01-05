import { Identifier, Name, OriginInfo, PhysicalDescription, TitleInfo } from '.'

export interface ModsMetadata {
	titleInfos?: TitleInfo[]
	name?: Name
	genre?: string
	physicalDescription?: PhysicalDescription
	originInfo?: OriginInfo
	identifiers?: Identifier[]
}
