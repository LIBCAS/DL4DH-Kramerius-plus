import React from 'react'
import Typography from '@material-ui/core/Typography'
import Grid from '@material-ui/core/Grid'
import { makeStyles } from '@material-ui/core/styles'

import { Params, Sort } from '../../models'
import { CheckboxField } from '../checkbox-field/checkbox-field'
import { TextField } from '../text-field/text-field'
import { SelectField } from '../select-field/select-field'
import { fieldOptions, sortOptions } from './publication-items'

type Props = {
	params: Params
	setParams: React.Dispatch<React.SetStateAction<Params>>
}

const useStyles = makeStyles(() => ({
	root: {
		marginTop: 20,
		marginBottom: 10,
		width: 400,
	},
}))

export const JSONParams = ({ setParams, params }: Props) => {
	const classes = useStyles()
	const { includeFields = [], excludeFields = [] } = params

	return (
		<div className={classes.root}>
			<Typography>Parametry stránek</Typography>

			<Grid container spacing={2}>
				<Grid item xs={6}>
					<SelectField<{ id: string; label: string }>
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

				<Grid item xs={6}>
					<SelectField<{ id: string; label: string }>
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
			{/* <ExportFilters filters={filters} setParams={setParams} /> */}
		</div>
	)
}
