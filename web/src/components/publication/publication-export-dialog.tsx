import { useState, useMemo } from 'react'
import RadioGroup from '@material-ui/core/RadioGroup'
import Radio from '@material-ui/core/Radio'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import { toast } from 'react-toastify'

import { DefaultDialog } from '../dialog/knav-dialog/knav-default-dialog'
import { DialogContentProps } from '../dialog/types'
import { Params, TeiParams } from '../../models'
import { JSONParams } from './publication-export-json'
import { TEIParams } from './publication-export-tei'

type ExportFormat = 'json' | 'tei' | 'csv' | 'tsv'

const exportPublication = async (
	id: string,
	format: ExportFormat,
	params: Params | TeiParams,
) => {
	const filters = (params.filters ?? []).map(f => ({
		field: f.field,
		value: f.value,
		operation: f.operation,
	}))

	const processedParams = {
		...params,
		filters,
		sort: [{ field: 'index', direction: params.sort }],
	}

	try {
		const response = await fetch(`/api/exports/${id}/${format}`, {
			method: 'POST',
			headers: new Headers({ 'Content-Type': 'application/json' }),
			body: JSON.stringify(processedParams),
		})

		return response
	} catch (e) {
		return {
			ok: false,
		}
	}
}

const defaultJSONParams: Params = {
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
	const [jsonParams, setJsonParams] = useState<Params>(defaultJSONParams)
	const [teiParams, setTeiParams] = useState<TeiParams>(defaultTeiParams)

	const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
		setFormat((event.target as HTMLInputElement).value as ExportFormat)
	}

	const requestParams = useMemo(
		() => (format === 'tei' ? teiParams : jsonParams),
		[format, jsonParams, teiParams],
	)

	const handleSubmitExport = async () => {
		const response = await exportPublication(
			initialValues!.id,
			format,
			requestParams,
		)

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
					label="TSV"
					value="tsv"
				/>
				<FormControlLabel
					control={<Radio color="primary" />}
					label="TEI"
					value="tei"
				/>
			</RadioGroup>

			{format === 'tei' ? (
				<TEIParams params={teiParams} setParams={setTeiParams} />
			) : (
				<JSONParams params={jsonParams} setParams={setJsonParams} />
			)}
		</DefaultDialog>
	)
}
