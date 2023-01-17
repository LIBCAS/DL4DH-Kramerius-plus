import OpenInNewIcon from '@mui/icons-material/OpenInNew'
import {
	IconButton,
	Table,
	TableBody,
	TableCell,
	TableContainer,
	TableRow,
} from '@mui/material'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { FC } from 'react'
import { Link } from 'react-router-dom'
import { useNavigate } from 'react-router-dom'

type Props = {
	jobs: KrameriusJobInstance[]
}

export const KrameriusJobTable: FC<Props> = ({ jobs }) => {
	const navigate = useNavigate()

	return (
		<TableContainer>
			<Table size="small">
				<TableBody>
					{jobs.map(job => (
						<TableRow
							key={job.id}
							component={Link}
							hover
							style={{ textDecoration: 'none ' }}
							sx={{ height: 20 }}
							to={`/jobs/${job.id}`}
							onClick={() => navigate(`/jobs/${job.id}`)}
						>
							<TableCell sx={{ padding: '0px 16px' }}>
								<IconButton
									size="small"
									onClick={() => navigate(`/jobs/${job.id}`)}
								>
									<OpenInNewIcon />
								</IconButton>
							</TableCell>
							<TableCell>{KrameriusJobMapping[job.jobType]}</TableCell>
							<TableCell>{job.executionStatus}</TableCell>
						</TableRow>
					))}
				</TableBody>
			</Table>
		</TableContainer>
	)
}
