import { DomainObject } from 'models/domain/domain-object'
import { EnrichmentChain } from './enrichment-chain'

export interface EnrichmentRequestItem extends DomainObject {
	publicationId: string
	publicationTitle: string
	model: string
	enrichmentChains: EnrichmentChain[]
}
