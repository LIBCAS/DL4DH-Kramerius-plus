import { Grid, Button } from '@mui/material'
import { UuidTextField } from './uuid-text-field'
import AddCircleIcon from '@mui/icons-material/AddCircle'

type Props = {
	fields: string[]
	removeUuidField: (index: number) => void
	changeUuidField: (index: number, value: string) => void
	addUuidField: () => void
}

export const UuidAccordionContent = ({
	fields,
	removeUuidField,
	changeUuidField,
	addUuidField,
}: Props) => {
	const handleRemoveField = (index: number) => () => {
		removeUuidField(index)
	}

	const handleFieldChange =
		(index: number) => (event: React.ChangeEvent<HTMLInputElement>) =>
			changeUuidField(index, event.target.value)

	function showAdornment(index: number) {
		return fields.length > 1 || index != 0
	}

	return (
		<Grid container spacing={2}>
			{fields.map((uuid, i) => (
				<Grid key={i} item xs={12}>
					<UuidTextField
						handleFieldChange={handleFieldChange(i)}
						handleRemoveField={handleRemoveField(i)}
						index={i}
						showAdornment={showAdornment(i)}
						value={uuid}
					/>
				</Grid>
			))}
			<Grid item xs={12}>
				<Button
					startIcon={<AddCircleIcon />}
					variant="text"
					onClick={addUuidField}
				>
					Přidat publikaci
				</Button>
			</Grid>
		</Grid>
	)
}
