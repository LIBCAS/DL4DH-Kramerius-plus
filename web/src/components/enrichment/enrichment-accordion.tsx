import { Box } from '@mui/system'
import { useState } from 'react'
import { EnrichmentJobEventConfig } from '../../models/job/config/enrichment/enrichment-job-event-config'
import { EnrichmentKrameriusJob } from '../../enums/enrichment-kramerius-job'
import { UuidAccordion } from './uuid-accordion/uuid-accordion'
import { ConfigAccordion } from './config-accordion/config-accordion'
import { NameAccordion } from './name-accordion/name-accordion'

export type PanelOpened = 'uuid' | 'config' | 'name' | 'none'

export type UuidAccordionProps = {
	fields: string[]
	removeUuidField: (index: number) => void
	changeUuidField: (index: number, value: string) => void
	addUuidField: () => void
}

export type ConfigAccordionProps = {
	configs: EnrichmentJobEventConfig[]
	onConfigClick: (index: number) => void
	onConfigRemove: (index: number) => void
	onNewConfigClick: (krameriusJob: EnrichmentKrameriusJob) => void
}

export type NameAccordionProps = {
	fieldValue?: string
	onFieldChange: (value: string) => void
}

type Props = {
	uuidProps: UuidAccordionProps
	configProps: ConfigAccordionProps
	nameProps: NameAccordionProps
}

export const EnrichmentAccordion = ({
	uuidProps,
	configProps,
	nameProps,
}: Props) => {
	const [expanded, setExpanded] = useState<PanelOpened>('none')

	const handleChange =
		// eslint-disable-next-line @typescript-eslint/no-unused-vars
		(panel: PanelOpened) => (_1: React.SyntheticEvent, _2: boolean) => {
			setExpanded(panel === expanded ? 'none' : panel)
		}

	return (
		<Box>
			<UuidAccordion
				isExpanded={expanded === 'uuid'}
				uuidProps={uuidProps}
				onChange={handleChange}
			/>
			<ConfigAccordion
				configProps={configProps}
				isExpanded={expanded === 'config'}
				onChange={handleChange}
			/>
			<NameAccordion
				isExpanded={expanded === 'name'}
				nameProps={nameProps}
				onChange={handleChange}
			/>
		</Box>
	)
}
