import React, { useState } from 'react'
import RadioGroup from '@material-ui/core/RadioGroup'
import Radio from '@material-ui/core/Radio'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import { toast } from 'react-toastify'

import { DefaultDialog } from '../dialog/knav-dialog/knav-default-dialog'
import { DialogContentProps } from '../dialog/types'
import { Params, TeiParams } from '../../models'
import { JSONParams } from './publication-export-json'
import { TEIParams } from './publication-export-tei'
import { Typography } from '@material-ui/core'
import { Box } from '@mui/system'
import { CsvExportJobEventConfig } from 'models/job/config/export/csv-export-job-event-config'
import { AltoExportJobEventConfig } from 'models/job/config/export/alto-export-job-event-config'
import { ExportJobEventConfig } from 'models/job/config/export/export-job-event-config'
import { JsonExportJobEventConfig } from 'models/job/config/export/json-export-job-event-config'
import { TeiExportJobEventConfig } from 'models/job/config/export/tei-export-job-event-config'
import { TextExportJobEventConfig } from 'models/job/config/export/text-export-job-event-config'

type ExportFormat = 'json' | 'tei' | 'csv' | 'alto' | 'text'

type Delimiter = '\t' | ','

const exportPublication = async (
	id: string,
	config: ExportJobEventConfig,
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

		const response = await exportPublication(initialValues!.id, config, format)

		if (response.ok) {
			toast('Operace prob??hla ??sp????n??', {
				type: 'success',
			})
		} else {
			toast('P??i pokusu o export publikace do??lo k chyb??', {
				type: 'error',
			})
		}

		onClose()
	}

	return (
		<DefaultDialog
			contentHeight={470}
			minWidth={400}
			title="V??b??r form??tu"
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
					<Typography variant="body1">Odd??lova??</Typography>
					<RadioGroup
						aria-label="export-format"
						name="format"
						value={delimiter}
						onChange={handleDelimiterChange}
					>
						<FormControlLabel
							control={<Radio color="primary" />}
							label="????rka"
							value=","
						/>
						<FormControlLabel
							control={<Radio color="primary" />}
							label="Tabul??tor"
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
