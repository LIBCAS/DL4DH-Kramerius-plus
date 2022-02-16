import React from 'react'
import Typography from '@material-ui/core/Typography'
import Grid from '@material-ui/core/Grid'
import { makeStyles } from '@material-ui/core/styles'

import { Sort, TeiParams } from '../../models'
import { CheckboxField } from '../checkbox-field/checkbox-field'
import { TextField } from '../text-field/text-field'
import { SelectField } from '../select-field/select-field'
import { AltoParam, PipeParam, TagParam } from '../../models/tei-params'
import {
	altoParamsOptions,
	nameTagParamsOptions,
	sortOptions,
	udPipeParamsOptions,
} from './publication-items'

type Props = {
	params: TeiParams
	setParams: React.Dispatch<React.SetStateAction<TeiParams>>
}

const useStyles = makeStyles(() => ({
	root: {
		marginTop: 20,
		marginBottom: 10,
		width: 400,
	},
}))

export const TEIParams = ({ setParams, params }: Props) => {
	const classes = useStyles()
	const { udPipeParams = [], altoParams = [], nameTagParams = [] } = params

	return (
		<div className={classes.root}>
			<Typography>Parametry stránek</Typography>

			<Grid container spacing={2}>
				<Grid item xs={6}>
					<SelectField<AltoParam>
						items={altoParamsOptions}
						label="altoParams"
						labelMapper={o => (o === '?' ? 'jiný znak' : o)}
						multiple
						name="altoParams"
						optionMapper={o => (o === '?' ? 'jiný znak' : o)}
						value={altoParams}
						onChange={v =>
							setParams(p => ({
								...p,
								altoParams: v as AltoParam[],
							}))
						}
					/>
				</Grid>
			</Grid>

			<Grid container spacing={2}>
				<Grid item xs={6}>
					<SelectField<TagParam>
						items={nameTagParamsOptions}
						label="nameTagParams"
						labelMapper={o => (o === '?' ? 'jiný znak' : o)}
						multiple
						name="nameTagParams"
						optionMapper={o => (o === '?' ? 'jiný znak' : o)}
						value={nameTagParams}
						onChange={v =>
							setParams(p => ({
								...p,
								nameTagParams: v as TagParam[],
							}))
						}
					/>
				</Grid>
			</Grid>

			<Grid container spacing={2}>
				<Grid item xs={6}>
					<SelectField<PipeParam>
						items={udPipeParamsOptions}
						label="udPipeParams"
						labelMapper={o => (o === '?' ? 'jiný znak' : o)}
						multiple
						name="udPipeParams"
						optionMapper={o => (o === '?' ? 'jiný znak' : o)}
						value={udPipeParams}
						onChange={v =>
							setParams(p => ({
								...p,
								udPipeParams: v as PipeParam[],
							}))
						}
					/>
				</Grid>
			</Grid>

			{/* <ExportFilters filters={filters} setParams={setParams} /> */}
		</div>
	)
}
