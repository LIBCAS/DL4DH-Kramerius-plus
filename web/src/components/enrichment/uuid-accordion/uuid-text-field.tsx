import {
	FormControl,
	InputLabel,
	OutlinedInput,
	InputAdornment,
	IconButton,
} from '@mui/material'
import DeleteIcon from '@mui/icons-material/Delete'

type Props = {
	index: number
	value: string
	handleRemoveField: () => void
	handleFieldChange: (event: React.ChangeEvent<HTMLInputElement>) => void
	showAdornment: boolean
}

export const UuidTextField = ({
	index,
	value,
	handleRemoveField,
	handleFieldChange,
	showAdornment,
}: Props) => {
	return (
		<FormControl key={index} fullWidth required size="small" variant="outlined">
			<InputLabel>UUID Publikace</InputLabel>
			<OutlinedInput
				endAdornment={
					showAdornment && (
						<InputAdornment position="end">
							<IconButton edge="end" onClick={handleRemoveField}>
								<DeleteIcon />
							</IconButton>
						</InputAdornment>
					)
				}
				label="UUID Publikace"
				type="text"
				value={value}
				onChange={handleFieldChange}
			/>
		</FormControl>
	)
}
