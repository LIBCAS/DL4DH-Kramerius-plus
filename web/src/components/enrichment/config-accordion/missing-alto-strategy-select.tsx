import {
	FormControl,
	InputLabel,
	Select,
	MenuItem,
	SelectChangeEvent,
} from '@mui/material'
import { MissingAltoStrategy } from '../../../enums/missing-alto-strategy'
import { MissingAltoStrategyMapping } from '../../../components/mappings/missing-alto-strategy-mapping'

type Props = {
	defaultValue: MissingAltoStrategy
	onChange: (event: SelectChangeEvent<MissingAltoStrategy>) => void
}

export const MissingAltoStrategySelect = ({
	defaultValue,
	onChange,
}: Props) => {
	return (
		<FormControl size="small" sx={{ m: 1, minWidth: 250 }}>
			<InputLabel>Strategie</InputLabel>
			<Select label="Strategie" value={defaultValue} onChange={onChange}>
				{Object.values(MissingAltoStrategy).map((strategy, i) => (
					<MenuItem key={i} value={strategy}>
						{MissingAltoStrategyMapping[strategy]}
					</MenuItem>
				))}
			</Select>
		</FormControl>
	)
}
