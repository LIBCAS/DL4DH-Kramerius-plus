import { EnrichmentKrameriusJob } from './enrichment-kramerius-job'
import { ExportKrameriusJob } from './export-kramerius-job'

export type KrameriusJob =
	| ExportKrameriusJob
	| EnrichmentKrameriusJob
	| 'CREATE_ENRICHMENT_REQUEST'
	| 'CREATE_EXPORT_REQUEST'
	| 'MERGE_JOB'
