import { KeyboardDateTimePicker } from '@material-ui/pickers'
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
		<KeyboardDateTimePicker
			ampm={false}
			cancelLabel="Zrušit"
			clearLabel="Vymazat"
			clearable
			disableFuture
			disabled={disabled}
			format="dd/MM/yyyy HH:mm"
			fullWidth
			inputVariant="outlined"
			label={label}
			showTodayButton
			size="small"
			todayLabel="Dnes"
			value={value}
			onChange={onChange}
		/>
	)
}
