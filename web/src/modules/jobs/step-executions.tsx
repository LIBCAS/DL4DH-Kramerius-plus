import OpenInNewIcon from '@mui/icons-material/OpenInNew'
import { Button } from '@mui/material'
import {
	DataGrid,
	GridRenderCellParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { PageBlock } from 'components/page-block'
import { StepExecution } from 'models'
import { StepError } from 'models/job/step-error'
import { FC } from 'react'
import { dateTimeFormatter, durationFormatter } from 'utils/formatters'

export const StepExecutions: FC<{
	executions?: StepExecution[]
	onStepExecutionClick: (executionId: number) => void
}> = ({ executions, onStepExecutionClick }) => {
	const renderErrorCell = (params: GridRenderCellParams) => {
		const errors = params.row['errors'] as StepError[]

		return (
			<Button
				disabled={errors.length === 0}
				endIcon={<OpenInNewIcon />}
				variant="text"
				onClick={() => {
					onStepExecutionClick(params.row['id'])
				}}
			>
				{errors.length}
			</Button>
		)
	}

	const columns = [
		{
			field: 'stepName',
			headerName: 'Název kroku',
			width: 250,
		},
		{
			field: 'startTime',
			headerName: 'Čas spuštění',
			width: 200,
			type: 'string',
			valueFormatter: dateTimeFormatter,
		},
		{
			field: 'endTime',
			headerName: 'Čas ukončení',
			width: 200,
			valueFormatter: dateTimeFormatter,
		},
		{
			field: 'duration',
			headerName: 'Trvání',
			width: 120,
			valueFormatter: durationFormatter,
		},
		{
			field: 'readCount',
			headerName: '# Čtení',
			width: 80,
			type: 'number',
		},
		{
			field: 'writeCount',
			headerName: '# Zápisů',
			width: 80,
			type: 'number',
		},
		{
			field: 'commitCount',
			headerName: '# Uložení',
			width: 90,
			type: 'number',
		},
		{
			field: 'skipped',
			headerName: '# Přeskočení',
			width: 110,
			valueGetter: (params: GridValueGetterParams) => {
				const readSkipCount = params.row['readSkipCount']
				const processSkipCount = params.row['processSkipCount']
				const writeSkipCount = params.row['writeSkipCount']

				return readSkipCount + processSkipCount + writeSkipCount
			},
			type: 'number',
		},
		{
			field: 'filterCount',
			headerName: '# Vynechání',
			width: 105,
			type: 'number',
		},
		{
			field: 'status',
			headerName: 'Stav',
			width: 130,
		},
		{
			field: 'exitCode',
			headerName: 'Výstup',
			width: 300,
		},
		{
			field: 'erros',
			headerName: '# Chyb',
			flex: 1,
			renderCell: renderErrorCell,
		},
	]

	return (
		<PageBlock title="Kroky zvoleného běhu">
			<DataGrid
				autoHeight
				columns={columns}
				density="compact"
				disableColumnFilter
				disableColumnMenu
				getRowClassName={() => 'data-grid-row'}
				getRowId={row => row.id}
				headerHeight={50}
				hideFooter
				rowHeight={50}
				rows={executions ? executions : []}
				sx={{ flexGrow: 1 }}
			/>
		</PageBlock>
	)
}
