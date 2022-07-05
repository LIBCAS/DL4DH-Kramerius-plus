import {
	Accordion,
	AccordionSummary,
	Typography,
	AccordionDetails,
	TextField,
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
				<TextField
					fullWidth
					label="Název úloh"
					size="small"
					type="text"
					value={nameProps.fieldValue}
					variant="outlined"
					onChange={handleNameChange}
				/>
			</AccordionDetails>
		</Accordion>
	)
}
