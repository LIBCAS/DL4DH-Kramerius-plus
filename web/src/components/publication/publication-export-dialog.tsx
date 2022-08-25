import React, { FC, useState } from 'react'
import { toast } from 'react-toastify'
import { DialogContentProps } from '../dialog/types'
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
import { CsvExportJobEventConfig } from 'models/job/config/export/csv-export-job-event-config'
import { AltoExportJobEventConfig } from 'models/job/config/export/alto-export-job-event-config'
import { ExportJobEventConfig } from 'models/job/config/export/export-job-event-config'
import { JsonExportJobEventConfig } from 'models/job/config/export/json-export-job-event-config'
import { TeiExportJobEventConfig } from 'models/job/config/export/tei-export-job-event-config'
import { TextExportJobEventConfig } from 'models/job/config/export/text-export-job-event-config'
import { exportPublication } from 'api/export-api'

export type ExportFormat = 'json' | 'tei' | 'csv' | 'alto' | 'text'

type Delimiter = '\t' | ','

const defaultParams: Params = {
	filters: [],
	includeFields: [],
}

const defaultTeiParams: TeiParams = {
	filters: [],
	includeFields: [],
	udPipeParams: [],
	nameTagParams: [],
	altoParams: [],
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
		let config: ExportJobEventConfig
		if (format === 'csv') {
			config = {
				params: params,
				delimiter: delimiter,
			} as CsvExportJobEventConfig
		} else if (format === 'json') {
			config = {
				params: params,
			} as JsonExportJobEventConfig
		} else if (format === 'tei') {
			config = {
				params: teiParams,
			} as TeiExportJobEventConfig
		} else if (format === 'alto') {
			config = {
				params: params,
			} as AltoExportJobEventConfig
		} else {
			config = {
				params: params,
			} as TextExportJobEventConfig
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

				{format === 'tei' ? (
					<TEIParams params={teiParams} setParams={setTeiParams} />
				) : (
					<JSONParams
						disabled={disabledParams}
						params={params}
						setParams={setParams}
					/>
				)}
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
