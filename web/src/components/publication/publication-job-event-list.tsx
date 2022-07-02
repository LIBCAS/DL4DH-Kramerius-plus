import { Paper } from '@mui/material'
import { DataGrid, GridRowParams } from '@mui/x-data-grid'
import { JobType } from 'enums/job-type'
import { KrameriusJob } from 'enums/kramerius-job'
import { JobEvent } from 'models/job/job-event'
import { listJobEvents } from 'modules/jobs/job-api'
import { useEffect, useState } from 'react'
import { useHistory } from 'react-router'
import { dateTimeFormatter } from 'utils/formatters'

type Props = {
	publicationId: string
	krameriusJob: KrameriusJob
}

export const PublicationJobEventList = ({
	publicationId,
	krameriusJob,
}: Props) => {
	const [rowCount, setRowCount] = useState<number>()
	const [jobEvents, setJobEvents] = useState<JobEvent[]>([])
	const [page, setPage] = useState<number>(0)
	const [rowCountState, setRowCountState] = useState<number | undefined>(
		rowCount,
	)
	const history = useHistory()

	const columns = [
		{
			field: 'id',
			headerName: 'ID',
			width: 320,
			type: 'string',
		},
		{
			field: 'created',
			headerName: 'Vytvořeno',
			width: 210,
			type: 'datetime',
			valueFormatter: dateTimeFormatter,
		},
		{
			field: 'lastExecutionStatus',
			headerName: 'Poslední stav',
			width: 150,
		},
	]

	useEffect(() => {
		async function fetchJobs() {
			const response = await listJobEvents(JobType.Enriching, page, 10, {
				publicationId: publicationId,
				jobType: krameriusJob,
			})

			if (response) {
				setJobEvents(response.results)
				setRowCount(response.total)
			}
		}
		fetchJobs()
	}, [page, publicationId, krameriusJob])

	useEffect(() => {
		setRowCountState(prevRowCountState =>
			rowCount !== undefined ? rowCount : prevRowCountState,
		)
	}, [rowCount, setRowCountState])

	const onPageChange = (page: number) => setPage(page)

	const onRowClick = (params: GridRowParams) => {
		history.push(`/jobs/${JobType.Enriching}/${params.row['id']}`)
	}

	return (
		<Paper>
			<DataGrid
				autoHeight={true}
				columns={columns}
				density="compact"
				disableColumnFilter
				disableColumnMenu
				disableSelectionOnClick
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
