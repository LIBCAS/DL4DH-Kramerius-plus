import {
	Paper,
	Box,
	Button,
	Grid,
	MenuItem,
	TextField,
	Typography,
} from '@mui/material'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { JobStatus } from 'enums/job-status'
import { ChangeEvent, useState } from 'react'
import { JobEventFilterDto } from './job-event-filter-dto'

type Props = {
	jobTypes: string[]
	onSubmit: (filter: JobEventFilterDto) => void
}

export const JobEventFilter = ({ jobTypes, onSubmit }: Props) => {
	const [filter, setFilter] = useState<JobEventFilterDto>(
		{} as JobEventFilterDto,
	)

	const handleUuidChange = (event: ChangeEvent<HTMLInputElement>) => {
		setFilter(prevFilter => ({
			...prevFilter,
			publicationId: event.target.value,
		}))
	}

	const handleJobTypeChange = (event: ChangeEvent<HTMLInputElement>) => {
		setFilter(prevFilter => ({
			...prevFilter,
			jobType: event.target.value,
		}))
	}

	const handleLastExecutionStatusChange = (
		event: ChangeEvent<HTMLInputElement>,
	) => {
		setFilter(prevFilter => ({
			...prevFilter,
			lastExecutionStatus: event.target.value,
		}))
	}

	const handleSubmit = () => {
		onSubmit(filter)
	}

	return (
		<Paper elevation={2}>
			<Box component="form" sx={{ p: 2 }}>
				<Grid container spacing={3}>
					<Grid item xs={12}>
						<Typography variant="h5">Filtrování</Typography>
					</Grid>
					<Grid item lg={3} md={5} sm={7} xs={9}>
						<TextField
							fullWidth
							label="UUID publikace"
							value={filter.publicationId}
							onChange={handleUuidChange}
						></TextField>
					</Grid>
					<Grid item lg={2} md={4} sm={6} xs={8}>
						<TextField
							fullWidth
							label="Typ úlohy"
							select
							onChange={handleJobTypeChange}
						>
							<MenuItem value={undefined}>Všechny</MenuItem>
							{jobTypes.map(jobType => (
								<MenuItem key={jobType} value={jobType}>
									{KrameriusJobMapping[jobType]}
								</MenuItem>
							))}
						</TextField>
					</Grid>
					<Grid item lg={2} md={4} sm={6} xs={8}>
						<TextField
							fullWidth
							label="Stav"
							select
							onChange={handleLastExecutionStatusChange}
						>
							<MenuItem value={undefined}>Všechny</MenuItem>
							{Object.values(JobStatus).map(jobStatus => (
								<MenuItem key={jobStatus} value={jobStatus}>
									{jobStatus}
								</MenuItem>
							))}
						</TextField>
					</Grid>
				</Grid>
				<Box sx={{ paddingTop: 4 }}>
					<Button color="primary" variant="contained" onClick={handleSubmit}>
						Filtrovat
					</Button>
				</Box>
			</Box>
		</Paper>
	)
}
