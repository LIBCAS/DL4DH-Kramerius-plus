import React from 'react'
import Typography from '@material-ui/core/Typography'
import Grid from '@material-ui/core/Grid'
import { makeStyles } from '@material-ui/core/styles'

import { TeiParams } from '../../models'
import { SelectField } from '../select-field/select-field'
import { AltoParam, UDPipeParam, NameTagParam } from '../../models/tei-params'
import {
	altoParamsOptions,
	nameTagParamsOptions,
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
	parameters: {
		marginBottom: '1rem',
	},
}))

export const TEIParams = ({ setParams, params }: Props) => {
	const classes = useStyles()
	const { udPipeParams = [], altoParams = [], nameTagParams = [] } = params

	return (
		<div className={classes.root}>
			<Typography className={classes.parameters}>Parametry stránek</Typography>

			<Grid container spacing={2}>
				<Grid item xs={12}>
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

				<Grid item xs={12}>
					<SelectField<NameTagParam>
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
								nameTagParams: v as NameTagParam[],
							}))
						}
					/>
				</Grid>

				<Grid item xs={12}>
					<SelectField<UDPipeParam>
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
								udPipeParams: v as UDPipeParam[],
							}))
						}
					/>
				</Grid>
			</Grid>

			{/* <ExportFilters filters={filters} setParams={setParams} /> */}
		</div>
	)
}
