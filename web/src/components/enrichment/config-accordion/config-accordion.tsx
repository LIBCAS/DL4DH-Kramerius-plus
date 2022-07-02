import {
	Accordion,
	AccordionSummary,
	Typography,
	AccordionDetails,
} from '@mui/material'
import ExpandMoreIcon from '@mui/icons-material/ExpandMore'
import { ConfigAccordionProps, PanelOpened } from '../enrichment-accordion'
import { ConfigAccordionContent } from './config-accordion-content'

type Props = {
	isExpanded: boolean
	onChange: (
		panel: PanelOpened,
	) => (_1: React.SyntheticEvent, _2: boolean) => void
	configProps: ConfigAccordionProps
}

export const ConfigAccordion = ({
	isExpanded,
	onChange,
	configProps,
}: Props) => {
	return (
		<Accordion
			elevation={4}
			expanded={isExpanded}
			onChange={onChange('config')}
		>
			<AccordionSummary
				aria-controls="panel1bh-content"
				expandIcon={<ExpandMoreIcon />}
				id="panel1bh-header"
			>
				<Typography sx={{ width: '33%', flexShrink: 0 }}>
					Konfigurace
				</Typography>
				<Typography sx={{ color: 'text.secondary' }}>
					Konfigurace spouštěných úloh
				</Typography>
			</AccordionSummary>
			<AccordionDetails>
				<ConfigAccordionContent {...configProps} />
			</AccordionDetails>
		</Accordion>
	)
}
