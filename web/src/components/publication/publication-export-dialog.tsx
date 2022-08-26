import React, { FC, useState } from 'react'
import { toast } from 'react-toastify'
import { Params, TeiParams } from '../../models'
import { JSONParams } from './publication-export-json'
import { TEIParams } from './publication-export-tei'
import {
	Typography,
	Box,
	Dialog,
	DialogTitle,
	DialogContent,
	DialogActions,
	RadioGroup,
	Radio,
	FormControlLabel,
	Button,
} from '@mui/material'
import { exportPublication } from 'api/export-api'
import { TeiExportParams } from 'models/tei-params'

export type ExportFormat = 'json' | 'tei' | 'csv' | 'alto' | 'text'

type Delimiter = '\t' | ','

const defaultParams: Params = {
	filters: [],
	includeFields: [],
}

const defaultTeiParams: TeiExportParams = {
	udPipeParams: [],
	nameTagParams: [],
	altoParams: [],
}

export interface ExportJobConfig {
	params: Params
}

interface CsvExportJobConfig extends ExportJobConfig {
	delimiter: Delimiter
}

interface TeiExportJobConfig extends ExportJobConfig {
	teiExportParams: TeiExportParams
}

export const PublicationExportDialog: FC<{
	onClose: () => void
	open: boolean
	publicationId: string
}> = ({ onClose, open, publicationId }) => {
	const [format, setFormat] = useState<ExportFormat>('json')
	const [delimiter, setDelimiter] = useState<Delimiter>(',')
	const [params, setParams] = useState<Params>(defaultParams)
	const [teiParams, setTeiParams] = useState<TeiParams>(defaultTeiParams)
	const [disabledParams, setDisabledParams] = useState<boolean>(false)

	const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
		const format = (event.target as HTMLInputElement).value as ExportFormat
		setFormat(format)

		if (format === 'alto' || format === 'text') {
			setDisabledParams(true)
		} else {
			setDisabledParams(false)
		}
	}

	const handleDelimiterChange = (
		event: React.ChangeEvent<HTMLInputElement>,
	) => {
		setDelimiter((event.target as HTMLInputElement).value as Delimiter)
	}

	const handleSubmitExport = async () => {
		let config
		if (format === 'csv') {
			config = {
				params: params,
				delimiter: delimiter,
			} as CsvExportJobConfig
		} else if (format === 'tei') {
			config = {
				params: params,
				teiExportParams: teiParams,
			} as TeiExportJobConfig
		} else {
			config = {
				params: params,
			} as ExportJobConfig
		}

		const response = await exportPublication(publicationId, config, format)

		if (response.ok) {
			toast('Operace proběhla úspěšně', {
				type: 'success',
			})
		} else {
			toast('Při pokusu o export publikace došlo k chybě', {
				type: 'error',
			})
		}

		onClose()
	}

	return (
		<Dialog
			fullWidth
			open={open}
			onClose={onClose}
			onSubmit={handleSubmitExport}
		>
			<DialogTitle>Výběr formátu</DialogTitle>

			<DialogContent>
				<RadioGroup
					aria-label="export-format"
					name="format"
					value={format}
					onChange={handleChange}
				>
					<FormControlLabel
						control={<Radio color="primary" />}
						label="JSON"
						value="json"
					/>
					<FormControlLabel
						control={<Radio color="primary" />}
						label="CSV"
						value="csv"
					/>
					<FormControlLabel
						control={<Radio color="primary" />}
						label="TEI"
						value="tei"
					/>
					<FormControlLabel
						control={<Radio color="primary" />}
						label="ALTO"
						value="alto"
					/>
					<FormControlLabel
						control={<Radio color="primary" />}
						label="TEXT"
						value="text"
					/>
				</RadioGroup>

				{format === 'csv' && (
					<Box sx={{ pt: 2 }}>
						<Typography variant="body1">Oddělovač</Typography>
						<RadioGroup
							aria-label="export-format"
							name="format"
							value={delimiter}
							onChange={handleDelimiterChange}
						>
							<FormControlLabel
								control={<Radio color="primary" />}
								label="Čárka"
								value=","
							/>
							<FormControlLabel
								control={<Radio color="primary" />}
								label="Tabulátor"
								value="	"
							/>
						</RadioGroup>
					</Box>
				)}

				{!disabledParams &&
					(format === 'tei' ? (
						<TEIParams params={teiParams} setParams={setTeiParams} />
					) : (
						<JSONParams params={params} setParams={setParams} />
					))}
			</DialogContent>
			<DialogActions>
				<Button color="inherit" onClick={onClose}>
					Zavřít
				</Button>
				<Button color="inherit" onClick={handleSubmitExport}>
					Potvrdit
				</Button>
			</DialogActions>
		</Dialog>
	)
}
