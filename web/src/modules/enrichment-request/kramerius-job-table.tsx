import {
	Box,
	Paper,
	Table,
	TableBody,
	TableCell,
	TableContainer,
	TableHead,
	TableRow,
	Typography,
} from '@mui/material'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { FC } from 'react'
import { useNavigate } from 'react-router-dom'

type Props = {
	jobs: KrameriusJobInstance[]
}

export const KrameriusJobTable: FC<Props> = ({ jobs }) => {
	const navigate = useNavigate()

	return (
		<Box sx={{ margin: 1 }}>
			<Typography component="div" gutterBottom variant="h6">
				Úlohy
			</Typography>
			<TableContainer component={Paper} variant="outlined">
				<Table size="small">
					<TableHead>
						<TableRow>
							<TableCell>Typ úlohy</TableCell>
							<TableCell>Stav</TableCell>
						</TableRow>
					</TableHead>
					<TableBody>
						{jobs.map(job => (
							<TableRow
								key={job.id}
								hover
								onClick={() => navigate(`/jobs/${job.id}`)}
							>
								<TableCell>{KrameriusJobMapping[job.jobType]}</TableCell>
								<TableCell>{job.executionStatus}</TableCell>
							</TableRow>
						))}
					</TableBody>
				</Table>
			</TableContainer>
		</Box>
	)
}
