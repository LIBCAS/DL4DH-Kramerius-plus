import { Paper } from '@material-ui/core'
import { DataGrid, GridRowParams } from '@mui/x-data-grid'
import { JobEvent } from 'models/job-event'
import { JobType } from 'models/job-type'
import { KrameriusJob } from 'models/kramerius-job'
import { useEffect, useState } from 'react'
import { dateTimeFormatter } from 'utils/formatters'
import { listJobEvents } from '../job-api'

type Props = {
	jobType: JobType
	onRowClick: (params: GridRowParams) => void
	krameriusJob?: KrameriusJob
	publicationId?: string
}

const columns = [
	{
		field: 'id',
		headerName: 'ID',
		width: 300,
		type: 'string',
	},
	{
		field: 'created',
		headerName: 'Vytvořeno',
		width: 200,
		type: 'datetime',
		valueFormatter: dateTimeFormatter,
	},
	{
		field: 'lastExecutionStatus',
		headerName: 'Stav posledního spuštění',
		flex: 1,
	},
]

export const JobEventList = ({
	jobType,
	onRowClick,
	krameriusJob,
	publicationId,
}: Props) => {
	const [rowCount, setRowCount] = useState<number>()
	const [jobEvents, setJobEvents] = useState<JobEvent[]>([])
	const [page, setPage] = useState<number>(0)
	const [rowCountState, setRowCountState] = useState<number | undefined>(
		rowCount,
	)

	useEffect(() => {
		async function fetchJobs() {
			const response = await listJobEvents(
				jobType,
				page,
				10,
				publicationId,
				krameriusJob,
			)

			if (response) {
				setJobEvents(response.results)
				setRowCount(response.total)
			}
		}
		fetchJobs()
	}, [page, krameriusJob, publicationId])

	useEffect(() => {
		setRowCountState(prevRowCountState =>
			rowCount !== undefined ? rowCount : prevRowCountState,
		)
	}, [rowCount, setRowCountState])

	const onPageChange = (page: number) => setPage(page)

	return (
		<Paper>
			<DataGrid
				autoHeight={true}
				columns={columns}
				disableColumnFilter
				disableColumnMenu
				pageSize={10}
				paginationMode="server"
				rowCount={rowCountState}
				rows={jobEvents}
				rowsPerPageOptions={[]}
				onPageChange={onPageChange}
				onRowClick={onRowClick}
			/>
		</Paper>
	)
}
