import { EnrichmentKrameriusJob } from 'enums/enrichment-kramerius-job'
import { EnrichmentJobConfig } from 'models/job/config/enrichment-job-config'
import { v4 as uuid } from 'uuid'

export enum EnrichmentProfile {
	BASIC = 'BASIC',
	UPDATE = 'UPDATE',
	COMPLETE = 'COMPLETE',
	CUSTOM = 'CUSTOM',
}

type EnrichmentProfileType = {
	type: EnrichmentProfile
	configs: EnrichmentJobConfig[]
	label: string
}

export enum EnrichmentStorageKeys {
	CUSTOM_CONFIGS = 'custom-enrichment-configs',
	ENRICHMENT_PROFILE = 'enrichment-profile',
}

export const enrichmentProfiles: EnrichmentProfileType[] = [
	{
		type: EnrichmentProfile.BASIC,
		configs: [
			{
				id: uuid(),
				override: false,
				pageErrorTolerance: 0,
				jobType: EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL,
			},
			{
				id: uuid(),
				override: false,
				pageErrorTolerance: 0,
				jobType: EnrichmentKrameriusJob.ENRICHMENT_TEI,
			},
		],
		label: 'Základ',
	},
	{
		type: EnrichmentProfile.UPDATE,
		configs: [
			{
				id: uuid(),
				override: true,
				pageErrorTolerance: 0,
				jobType: EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL,
			},
			{
				id: uuid(),
				override: true,
				pageErrorTolerance: 0,
				jobType: EnrichmentKrameriusJob.ENRICHMENT_TEI,
			},
		],
		label: 'Aktualizace',
	},
	{
		type: EnrichmentProfile.COMPLETE,
		configs: [
			{
				id: uuid(),
				override: true,
				pageErrorTolerance: 0,
				jobType: EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL,
			},
			{
				id: uuid(),
				override: true,
				pageErrorTolerance: 0,
				jobType: EnrichmentKrameriusJob.ENRICHMENT_TEI,
			},
			{
				id: uuid(),
				override: true,
				pageErrorTolerance: 0,
				jobType: EnrichmentKrameriusJob.ENRICHMENT_NDK,
			},
		],
		label: 'Komplet',
	},
	{
		type: EnrichmentProfile.CUSTOM,
		configs: [],
		label: 'Vlastní',
	},
]
