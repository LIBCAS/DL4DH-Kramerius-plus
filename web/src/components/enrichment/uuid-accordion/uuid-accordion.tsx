import {
	Accordion,
	AccordionSummary,
	Typography,
	AccordionDetails,
} from '@mui/material'
import { PanelOpened, UuidAccordionProps } from '../enrichment-accordion'
import ExpandMoreIcon from '@mui/icons-material/ExpandMore'
import { UuidAccordionContent } from './uuid-accordion-content'

type Props = {
	isExpanded: boolean
	onChange: (
		panel: PanelOpened,
	) => (_1: React.SyntheticEvent, _2: boolean) => void
	uuidProps: UuidAccordionProps
}

export const UuidAccordion = ({ isExpanded, onChange, uuidProps }: Props) => {
	return (
		<Accordion elevation={4} expanded={isExpanded} onChange={onChange('uuid')}>
			<AccordionSummary expandIcon={<ExpandMoreIcon />}>
				<Typography sx={{ width: '33%', flexShrink: 0 }}>Publikace</Typography>
				<Typography sx={{ color: 'text.secondary' }}>
					Definice množiny publikací na obohacení
				</Typography>
			</AccordionSummary>
			<AccordionDetails>
				<UuidAccordionContent {...uuidProps} />
			</AccordionDetails>
		</Accordion>
	)
}
