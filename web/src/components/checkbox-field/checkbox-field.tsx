import React, { useCallback } from 'react'
import { makeStyles, Theme, createStyles } from '@material-ui/core/styles'
import FormControl from '@material-ui/core/FormControl'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import Checkbox from '@material-ui/core/Checkbox'

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		root: {
			color: theme.palette.primary.main,
		},
		label: {
			padding: '0 5px',
			zIndex: 100,
			backgroundColor: '#f6f6f6',
			color: 'black',
		},
	}),
)

export interface CheckboxFieldProps {
	/**
	 * Name of input.
	 */
	name: string

	/**
	 * Label of input.
	 */
	label?: string | JSX.Element

	/**
	 * Bool value of input.
	 */
	value: boolean

	/**
	 * Change handler.
	 */
	onChange: (value: boolean) => void

	/**
	 * Disable flag.
	 */
	disabled?: boolean
	/**
	 * Indeterminate.
	 */
	indeterminate?: boolean
}

export function CheckboxField({
	name,
	label,
	value,
	onChange,
	disabled,
	indeterminate,
}: CheckboxFieldProps) {
	const classes = useStyles()

	/**
	 *
	 */
	const handleChange = useCallback(
		(e: React.ChangeEvent<HTMLInputElement>) => {
			const value = e.target.checked

			onChange(value)
		},
		[onChange],
	)

	return (
		<FormControl style={{ marginBottom: 8 }} variant="outlined">
			<FormControlLabel
				control={
					<Checkbox
						checked={value}
						className={classes.root}
						color="primary"
						disabled={disabled}
						indeterminate={indeterminate}
						name={name}
						onChange={handleChange}
					/>
				}
				label={label}
			/>
		</FormControl>
	)
}
