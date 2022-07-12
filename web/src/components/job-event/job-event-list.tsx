import { Paper, Button } from '@mui/material'
import {
	DataGrid,
	GridRenderCellParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { JobType } from 'enums/job-type'
import { JobEvent } from 'models/job/job-event'
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { dateTimeFormatter } from 'utils/formatters'
import { listJobEvents } from '../../modules/jobs/job-api'
import { JobEventFilterDto } from '../../modules/jobs/job-event/job-event-filter-dto'

type Props = {
	jobType: JobType
	filter?: JobEventFilterDto
}

const krameriusJobGetter = (params: GridValueGetterParams) => {
	return params.row['config']['krameriusJob']
}

const nameGetter = (params: GridValueGetterParams) => {
	return params.row['jobName'] ?? '{Bez názvu}'
}

export const JobEventList = ({ jobType, filter }: Props) => {
	const [rowCount, setRowCount] = useState<number>()
	const [jobEvents, setJobEvents] = useState<JobEvent[]>([])
	const [page, setPage] = useState<number>(0)
	const [rowCountState, setRowCountState] = useState<number | undefined>(
		rowCount,
	)

	const columns = [
		{
			field: 'id',
			headerName: 'ID',
			width: 300,
			type: 'string',
		},
		{
			field: 'publicationId',
			headerName: 'UUID publikace',
			width: 340,
			type: 'string',
		},
		{
			field: 'created',
			headerName: 'Vytvořeno',
			width: 180,
			type: 'datetime',
			valueFormatter: dateTimeFormatter,
		},
		{
			field: 'krameriusJob',
			headerName: 'Typ úlohy',
			width: 200,
			valueGetter: krameriusJobGetter,
		},
		{
			field: 'lastExecutionStatus',
			headerName: 'Poslední stav',
			width: 120,
		},
		{
			field: 'jobName',
			headerName: 'Název',
			width: 150,
			type: 'string',
			valueGetter: nameGetter,
		},
		{
			field: 'action',
			headerName: 'Akce',
			flex: 1,
			sortable: false,
			renderCell: (params: GridRenderCellParams) => {
				return (
					<Link
						style={{ textDecoration: 'none ' }}
						to={`${jobType}/${params.row['id']}`}
					>
						<Button color="primary" variant="contained">
							Detail
						</Button>
					</Link>
				)
			},
		},
	]

	useEffect(() => {
		async function fetchJobs() {
			const response = await listJobEvents(jobType, page, 10, filter)

			if (response) {
				setJobEvents(response.results)
				setRowCount(response.total)
			}
		}
		fetchJobs()
	}, [page, filter, jobType])

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
			/>
		</Paper>
	)
}
