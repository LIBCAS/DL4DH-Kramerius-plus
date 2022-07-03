import {
	Accordion,
	AccordionSummary,
	Typography,
	AccordionDetails,
	FormControl,
	InputLabel,
	OutlinedInput,
} from '@mui/material'
import ExpandMoreIcon from '@mui/icons-material/ExpandMore'
import { NameAccordionProps, PanelOpened } from '../enrichment-accordion'
import { ChangeEvent } from 'react'

type Props = {
	isExpanded: boolean
	onChange: (
		panel: PanelOpened,
	) => (_1: React.SyntheticEvent, _2: boolean) => void
	nameProps: NameAccordionProps
}

export const NameAccordion = ({ isExpanded, onChange, nameProps }: Props) => {
	const handleNameChange = (event: ChangeEvent<HTMLInputElement>) => {
		nameProps.onFieldChange(event.target.value)
	}

	return (
		<Accordion elevation={4} expanded={isExpanded} onChange={onChange('name')}>
			<AccordionSummary expandIcon={<ExpandMoreIcon />} id="panel1bh-header">
				<Typography sx={{ width: '33%', flexShrink: 0 }}>Název</Typography>
				<Typography sx={{ color: 'text.secondary' }}>
					Definice názvu vytvářených úloh (nepovinné)
				</Typography>
			</AccordionSummary>
			<AccordionDetails>
				<FormControl fullWidth size="small" variant="outlined">
					<InputLabel>Název úloh</InputLabel>
					<OutlinedInput
						label="Název úloh"
						type="text"
						value={nameProps.fieldValue}
						onChange={handleNameChange}
					/>
				</FormControl>
			</AccordionDetails>
		</Accordion>
	)
}
