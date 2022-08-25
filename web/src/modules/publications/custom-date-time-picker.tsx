import { TextField } from '@mui/material'
import { DateTimePicker } from '@mui/x-date-pickers'
import { FC } from 'react'

type Props = {
	value: Date | null
	label: string
	disabled?: boolean
	onChange: (value: Date | null) => void
}

export const CustomDateTimePicker: FC<Props> = ({
	value,
	label,
	disabled,
	onChange,
}) => {
	return (
		<DateTimePicker
			ampm={false}
			disableFuture
			disabled={disabled}
			inputFormat="dd/MM/yyyy HH:mm"
			label={label}
			renderInput={params => <TextField {...params} fullWidth size="small" />}
			value={value}
			onChange={onChange}
		/>
	)
}
