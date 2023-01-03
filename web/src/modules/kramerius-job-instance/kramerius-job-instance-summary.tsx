import {
	Paper,
	Table,
	TableBody,
	TableCell,
	TableContainer,
	TableHead,
	TableRow,
} from '@mui/material'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { FC } from 'react'

type Props = {
	job: KrameriusJobInstance
}

export const KrameriusJobInstanceSummary: FC<Props> = ({ job }) => {
	return (
		<TableContainer component={Paper} variant="outlined">
			<Table size="small">
				<TableHead>
					<TableRow>
						<TableCell>Typ úlohy</TableCell>
						<TableCell>Stav úlohy</TableCell>
					</TableRow>
				</TableHead>
				<TableBody>
					<TableRow>
						<TableCell>{KrameriusJobMapping[job.jobType]}</TableCell>
						<TableCell>{job.executionStatus}</TableCell>
					</TableRow>
				</TableBody>
			</Table>
		</TableContainer>
	)
}
