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
import { ExportJobConfig } from 'models/job/config/export-job-config'
import { ExportCsvJobConfig } from 'models/job/config/export-csv-job-config'
import { ExportTeiJobConfig } from 'models/job/config/export-tei-job-config'
import { v4 } from 'uuid'
import { ExportKrameriusJob } from 'enums/export-kramerius-job'

export type ExportFormat = 'json' | 'tei' | 'csv' | 'alto' | 'text'

type Delimiter = '\t' | ','

const defaultParams: Params = {
	filters: [],
	sorting: [],
	includeFields: [],
	excludeFields: [],
}

const defaultTeiParams: TeiParams = {
	udPipeParams: [],
	nameTagParams: [],
	altoParams: [],
}

export const PublicationExportDialog: FC<{
	onClose: () => void
	open: boolean
	publicationIds: string[]
}> = ({ onClose, open, publicationIds }) => {
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
		const callExport = async (config: ExportJobConfig) => {
			const response = await exportPublication(publicationIds, config)

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

		if (format === 'csv') {
			callExport({
				id: v4(),
				params: params,
				delimiter: delimiter,
				jobType: 'EXPORT_CSV',
			} as ExportCsvJobConfig)
		} else if (format === 'tei') {
			callExport({
				id: v4(),
				jobType: 'EXPORT_TEI',
				params: params,
				teiParams: teiParams,
			} as ExportTeiJobConfig)
		} else if (format === 'json') {
			callExport({
				params: params,
				jobType: 'EXPORT_JSON',
			} as ExportJobConfig)
		} else if (format === 'alto') {
			callExport({
				id: v4(),
				params: params,
				exportFormat: format,
				jobType: ExportKrameriusJob.EXPORT_ALTO,
			})
		} else if (format === 'text') {
			callExport({
				id: v4(),
				params: params,
				exportFormat: format,
				jobType: ExportKrameriusJob.EXPORT_TEXT,
			})
		}
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
