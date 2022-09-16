import {
	Button,
	ButtonGroup,
	Grid,
	Paper,
	Table,
	TableBody,
	TableCell,
	TableContainer,
	TableHead,
	TableRow,
	Typography,
} from '@mui/material'
import { download } from 'api/file-ref-api'
import { Export } from 'models'
import { FC } from 'react'
import { useNavigate } from 'react-router-dom'

type Props = {
	exports: Export[]
}

export const ExportRequestExportsList: FC<Props> = ({ exports }) => {
	const navigate = useNavigate()

	const onDetailClick = (jobEventId: string) => () => {
		navigate(`/jobs/exporting/${jobEventId}`)
	}

	return (
		<Grid container spacing={2} sx={{ p: 2 }}>
			<Grid item xs={12}>
				<Typography variant="h6">Plány obohacování</Typography>
			</Grid>
			<Grid item xs={12}>
				<TableContainer component={Paper}>
					<Table aria-label="exports-table" size="small">
						<TableHead>
							<TableRow>
								<TableCell>UUID publikace</TableCell>
								<TableCell>Název publikace</TableCell>
								<TableCell>Stav úlohy</TableCell>
								<TableCell>Akce</TableCell>
							</TableRow>
						</TableHead>
						<TableBody>
							{exports.map(e => (
								<TableRow key={e.id}>
									<TableCell>{e.publicationId}</TableCell>
									<TableCell>{e.publicationTitle}</TableCell>
									<TableCell>{e.jobEvent.lastExecutionStatus}</TableCell>
									<TableCell>
										<ButtonGroup disableElevation>
											<Button
												variant="contained"
												onClick={onDetailClick(e.jobEvent.id)}
											>
												Detail
											</Button>
											<Button
												disabled={!e.fileRef}
												variant="contained"
												onClick={download(e.fileRef?.id ?? '')}
											>
												Stáhnout
											</Button>
										</ButtonGroup>
									</TableCell>
								</TableRow>
							))}
						</TableBody>
					</Table>
				</TableContainer>
			</Grid>
		</Grid>
	)
}
