import React, { useCallback } from 'react'
import MuiTextField from '@material-ui/core/TextField'
import { makeStyles, createStyles } from '@material-ui/core/styles'
import InputLabel from '@material-ui/core/InputLabel'
import FormControl from '@material-ui/core/FormControl'

const useStyles = makeStyles(() =>
	createStyles({
		root: {
			width: '100%',
			marginBottom: 8,
		},
		label: {
			padding: '0 5px',
			zIndex: 100,
			backgroundColor: '#fff',
			color: 'black',
			fontSize: 16,
			fontWeight: 'bold',
		},
	}),
)

export interface TextFieldProps {
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
	value: string | number

	/**
	 * Change handler.
	 */
	onChange: (value: string | number) => void

	/**
	 *
	 */
	type?: string

	/**
	 *
	 */
	disabled?: boolean

	multiline?: boolean

	formControlStyles?: React.CSSProperties
}

export function TextField({
	label,
	value,
	onChange,
	type = 'text',
	disabled,
	multiline = false,
	formControlStyles = {},
}: TextFieldProps) {
	const classes = useStyles()

	/**
	 *
	 */
	const handleChange = useCallback(
		(e: React.ChangeEvent<HTMLInputElement>) => {
			const value = e.target.value

			onChange(value)
		},
		[onChange],
	)

	return (
		<FormControl
			className={classes.root}
			style={formControlStyles}
			variant="outlined"
		>
			<InputLabel className={classes.label} disabled={disabled} shrink>
				{label}
			</InputLabel>
			<MuiTextField
				InputLabelProps={{
					style: { color: '#000' },
					shrink: true,
				}}
				InputProps={{
					autoComplete: 'off',
				}}
				classes={{
					root: classes.root,
				}}
				color="primary"
				disabled={disabled}
				error={false}
				lang="cs"
				multiline={multiline}
				rows={5}
				spellCheck={true}
				type={type}
				value={value}
				variant="outlined"
				onChange={handleChange}
			/>
		</FormControl>
	)
}
