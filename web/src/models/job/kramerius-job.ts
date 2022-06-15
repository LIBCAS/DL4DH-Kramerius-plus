import { EnrichmentKrameriusJob } from './enrichment-kramerius-job'
import { ExportKrameriusJob } from './export-kramerius-job'

export type KrameriusJob = ExportKrameriusJob | EnrichmentKrameriusJob
