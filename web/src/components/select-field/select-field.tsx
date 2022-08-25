import {
	Select,
	MenuItem,
	InputLabel,
	FormControl,
	SelectClassKey,
	SelectChangeEvent,
	OutlinedInput,
} from '@mui/material'
import { v4 } from 'uuid'
import { get } from 'lodash'

// const useStyles = makeStyles(() =>
// 	createStyles({
// 		root: {
// 			width: '100%',
// 			marginBottom: 8,
// 		},
// 		label: {
// 			padding: '0 5px',
// 			zIndex: 100,
// 			backgroundColor: '#fff',
// 			color: 'black',
// 			fontSize: 16,
// 			fontWeight: 'bold',
// 		},
// 	}),
// )

export interface Item {
	/**
	 *
	 */
	id?: string

	/**
	 *
	 */
	label?: string
}

export interface SelectFieldProps<TOption extends Item> {
	/**
	 * Name of input.
	 */
	name: string

	/**
	 * Label of input.
	 */
	label?: string

	/**
	 * String or number value of input.
	 */
	value: string | string[]

	/**
	 * Change handler.
	 */
	onChange: (value: string | string[] | TOption | TOption[]) => void

	/**
	 *
	 */
	items: TOption[]

	/**
	 *
	 */
	disabled?: boolean

	/**
	 *
	 */
	additionalClasses?: Partial<Record<SelectClassKey, string>>

	/**
	 * Multiple choices.
	 */
	multiple?: boolean

	/**
	 * Returns whole object.
	 *
	 * Default is false.
	 */
	valueIsObject?: boolean

	optionMapper?: (o: TOption) => string

	labelMapper?: (o: TOption) => string | undefined
}

export function SelectField<TOption>({
	label,
	value,
	onChange,
	items = [],
	disabled,
	multiple = false,
	optionMapper = o => get(o, 'id', v4()),
	labelMapper = o => get(o, 'label'),
}: SelectFieldProps<TOption>) {
	/**
	 * Change handler.
	 */
	const handleChange = (event: SelectChangeEvent<typeof value>) => {
		const selectValue = event.target.value

		onChange(
			typeof selectValue === 'string' ? selectValue.split(',') : selectValue,
		)
	}

	return (
		<FormControl fullWidth variant="outlined">
			<InputLabel disabled={!!disabled} id="select-label">
				{label}
			</InputLabel>
			<Select
				color="primary"
				disabled={!!disabled}
				error={false}
				fullWidth
				input={<OutlinedInput label={label} />}
				label={label}
				labelId="select-label"
				lang="cs"
				multiple={multiple}
				spellCheck={true}
				value={value}
				onChange={handleChange}
			>
				{items.map((item, index) => (
					<MenuItem key={optionMapper(item)} value={optionMapper(item)}>
						{labelMapper(item) ?? `[${index + 1}]`}
					</MenuItem>
				))}
			</Select>
		</FormControl>
	)
}
