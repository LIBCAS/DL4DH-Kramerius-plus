import {
	Box,
	Button,
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
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { CollapsibleTableRow } from 'components/table/collapsible-table-row'
import { JobPlan } from 'models/job-plan'
import { FC } from 'react'
import { useNavigate } from 'react-router'

type Props = {
	plans: JobPlan[]
}

export const EnrichmentRequestPlans: FC<Props> = ({ plans }) => {
	const navigate = useNavigate()

	const onDetailClick = (jobEventId: string) => () => {
		navigate(`/jobs/enriching/${jobEventId}`)
	}

	const planToRowMapper = (plan: JobPlan): string[] => {
		const uuid = plan.scheduledJobEvents[0].jobEvent.publicationId
		const numberOfJobs = plan.scheduledJobEvents.length
		const lastJobEventStatus = plan.scheduledJobEvents
			.sort((a, b) => a.order - b.order)
			.at(-1)?.jobEvent.lastExecutionStatus
		return [uuid, numberOfJobs.toString(), lastJobEventStatus ?? '']
	}

	const planToChildComponentMapper = (plan: JobPlan) => {
		return (
			<Box sx={{ margin: 3 }}>
				<Typography component="div" gutterBottom variant="h6">
					Úlohy v pláne
				</Typography>
				<Table aria-label="job-events" size="small">
					<TableHead>
						<TableRow>
							<TableCell variant="head">Typ úlohy</TableCell>
							<TableCell variant="head">Pořadí úlohy v plánu</TableCell>
							<TableCell variant="head">Poslední stav</TableCell>
							<TableCell variant="head">Detail</TableCell>
						</TableRow>
					</TableHead>
					<TableBody>
						{plan.scheduledJobEvents
							.sort((a, b) => a.order - b.order)
							.map(scheduledJobEvent => (
								<TableRow key={scheduledJobEvent.id}>
									<TableCell component="th" scope="row">
										{
											KrameriusJobMapping[
												scheduledJobEvent.jobEvent.config.krameriusJob
											]
										}
									</TableCell>
									<TableCell>{scheduledJobEvent.order + 1}</TableCell>
									<TableCell>
										{scheduledJobEvent.jobEvent.lastExecutionStatus}
									</TableCell>
									<TableCell>
										<Button
											color="primary"
											variant="contained"
											onClick={onDetailClick(scheduledJobEvent.jobEvent.id)}
										>
											Detail
										</Button>
									</TableCell>
								</TableRow>
							))}
					</TableBody>
				</Table>
			</Box>
		)
	}

	return (
		<Grid container spacing={2} sx={{ p: 2 }}>
			<Grid item xs={12}>
				<Typography variant="h6">Plány obohacování</Typography>
			</Grid>
			<Grid item xs={12}>
				<TableContainer component={Paper}>
					<Table aria-label="collapsible table" size="small">
						<TableHead>
							<TableRow>
								<TableCell />
								<TableCell>UUID publikace</TableCell>
								<TableCell>Počet úloh v pláne</TableCell>
								<TableCell>Stav poslední úlohy</TableCell>
							</TableRow>
						</TableHead>
						<TableBody>
							{plans.map(plan => (
								<CollapsibleTableRow
									key={plan.id}
									values={planToRowMapper(plan)}
								>
									{planToChildComponentMapper(plan)}
								</CollapsibleTableRow>
							))}
						</TableBody>
					</Table>
				</TableContainer>
			</Grid>
		</Grid>
	)
}
