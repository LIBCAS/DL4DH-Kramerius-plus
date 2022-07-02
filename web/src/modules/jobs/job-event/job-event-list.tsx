import { Paper, Button } from '@material-ui/core'
import {
	DataGrid,
	GridRenderCellParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { JobType } from 'enums/job-type'
import { JobEvent } from 'models/job/job-event'
import { useEffect, useState } from 'react'
import { useHistory } from 'react-router'
import { dateTimeFormatter } from 'utils/formatters'
import { listJobEvents } from '../job-api'
import { JobEventFilterDto } from './job-event-filter-dto'

type Props = {
	jobType: JobType
	filter?: JobEventFilterDto
}

const krameriusJobGetter = (params: GridValueGetterParams) => {
	return params.row['config']['krameriusJob']
}

const nameGetter = (params: GridValueGetterParams) => {
	return params.row['jobName'] ?? 'Bez názvu'
}

export const JobEventList = ({ jobType, filter }: Props) => {
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
			field: 'publicationId',
			headerName: 'UUID publikace',
			width: 360,
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
			field: 'krameriusJob',
			headerName: 'Typ úlohy',
			width: 240,
			valueGetter: krameriusJobGetter,
		},
		{
			field: 'lastExecutionStatus',
			headerName: 'Poslední stav',
			width: 150,
		},
		{
			field: 'jobName',
			headerName: 'Název',
			width: 200,
			type: 'string',
			valueGetter: nameGetter,
		},
		{
			field: 'action',
			headerName: 'Akce',
			flex: 1,
			sortable: false,
			renderCell: (params: GridRenderCellParams) => {
				const onClick = () => {
					history.push(`${jobType}/${params.row['id']}`)
				}
				return (
					<Button color="primary" variant="contained" onClick={onClick}>
						Detail
					</Button>
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
