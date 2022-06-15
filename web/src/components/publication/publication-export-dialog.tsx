import React, { useState, useMemo } from 'react'
import RadioGroup from '@material-ui/core/RadioGroup'
import Radio from '@material-ui/core/Radio'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import { toast } from 'react-toastify'

import { DefaultDialog } from '../dialog/knav-dialog/knav-default-dialog'
import { DialogContentProps } from '../dialog/types'
import { Params, TeiParams } from '../../models'
import { JSONParams } from './publication-export-json'
import { TEIParams } from './publication-export-tei'
import { ExportJobEventConfigCreateDto } from 'models/job/config/dto/export/export-job-event-config-create-dto'
import { JsonExportJobEventConfigCreateDto } from 'models/job/config/dto/export/json-export-job-event-config-create-dto'
import { CsvExportJobEventConfigCreateDto } from 'models/job/config/dto/export/csv-export-job-event-config-create-dto'
import { TeiExportJobEventConfigCreateDto } from 'models/job/config/dto/export/tei-export-job-event-config-create-dto'
import { Typography } from '@material-ui/core'
import { Box } from '@mui/system'
import { ExportKrameriusJob } from 'models/job/export-kramerius-job'
import { AltoExportJobEventConfigCreateDto } from 'models/job/config/dto/export/alto-export-job-event-config-create-dto'
import { TextExportJobEventConfigCreateDto } from 'models/job/config/dto/export/text-export-job-event-config-create-dto'

type ExportFormat = 'json' | 'tei' | 'csv' | 'alto' | 'text'

type Delimiter = '\t' | ','

const exportPublication = async (
	id: string,
	config: ExportJobEventConfigCreateDto,
	format: ExportFormat,
) => {
	// const filters = (params.filters ?? []).map(f => ({
	// 	field: f.field,
	// 	value: f.value,
	// 	operation: f.operation,
	// }))

	// const processedParams = {
	// 	...params,
	// 	filters,
	// 	sort: [{ field: 'index', direction: params.sort }],
	// }

	try {
		const response = await fetch(`/api/exports/${id}/${format}`, {
			method: 'POST',
			headers: new Headers({ 'Content-Type': 'application/json' }),
			body: JSON.stringify(config),
		})

		return response
	} catch (e) {
		return {
			ok: false,
		}
	}
}

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

export const PublicationExportDialog = ({
	initialValues,
	onClose,
}: DialogContentProps<{
	id: string
}>) => {
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
		let config: ExportJobEventConfigCreateDto
		if (format === 'csv') {
			config = {
				krameriusJob: ExportKrameriusJob.EXPORT_CSV,
				params: params,
				delimiter: delimiter,
			} as CsvExportJobEventConfigCreateDto
		} else if (format === 'json') {
			config = {
				krameriusJob: ExportKrameriusJob.EXPORT_JSON,
				params: params,
			} as JsonExportJobEventConfigCreateDto
		} else if (format === 'tei') {
			config = {
				krameriusJob: ExportKrameriusJob.EXPORT_TEI,
				params: teiParams,
			} as TeiExportJobEventConfigCreateDto
		} else if (format === 'alto') {
			config = {
				krameriusJob: ExportKrameriusJob.EXPORT_ALTO,
				params: params,
			} as AltoExportJobEventConfigCreateDto
		} else {
			config = {
				krameriusJob: ExportKrameriusJob.EXPORT_TEXT,
				params: params,
			} as TextExportJobEventConfigCreateDto
		}

		const response = await exportPublication(initialValues!.id, config, format)

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
		<DefaultDialog
			contentHeight={470}
			minWidth={400}
			title="Výběr formátu"
			onSubmit={handleSubmitExport}
		>
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
		</DefaultDialog>
	)
}
