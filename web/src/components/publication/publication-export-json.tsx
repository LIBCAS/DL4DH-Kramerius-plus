import React from 'react'
import { Params } from '../../models'
import { SelectField } from '../select-field/select-field'
import { fieldOptions } from './publication-items'
import { Grid, Box, Typography } from '@mui/material'

type Props = {
	params: Params
	setParams: React.Dispatch<React.SetStateAction<Params>>
	disabled: boolean
}

export const JSONParams = ({ setParams, params, disabled }: Props) => {
	const { includeFields = [], excludeFields = [] } = params

	return (
		<Box>
			<Typography paddingBottom={2} paddingTop={2}>
				Parametry stránek
			</Typography>

			<Grid container spacing={2}>
				<Grid item xs={12}>
					<SelectField<{ id: string; label: string }>
						disabled={disabled || excludeFields.length > 0}
						items={fieldOptions}
						label="Zahrnout pole"
						multiple
						name="includeFields"
						optionMapper={o => o.id}
						value={includeFields}
						onChange={v =>
							setParams(p => ({
								...p,
								includeFields: v as string[],
							}))
						}
					/>
				</Grid>

				<Grid item xs={12}>
					<SelectField<{ id: string; label: string }>
						disabled={disabled || includeFields.length > 0}
						items={fieldOptions}
						label="Nezahrnout pole"
						multiple
						name="excludeFields"
						optionMapper={o => o.id}
						value={excludeFields}
						onChange={v =>
							setParams(p => ({
								...p,
								excludeFields: v as string[],
							}))
						}
					/>
				</Grid>
			</Grid>
		</Box>
	)
}
