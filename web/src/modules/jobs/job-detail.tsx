import { Grid, makeStyles, Paper } from '@material-ui/core'
import { JobInstance } from 'models/job-instance'
import { JobInstanceDetail } from './job-instance/job-instance-detail'

type Props = {
	jobInstance: JobInstance
}

const useStyles = makeStyles(() => ({
	paper: {
		padding: '20px',
	},
}))

export const JobDetail = ({ jobInstance }: Props) => {
	const classes = useStyles()

	return (
		<Paper className={classes.paper} elevation={4}>
			<Grid container xs={12}>
				{jobInstance && (
					<Grid item style={{ width: '100%' }}>
						<JobInstanceDetail job={jobInstance} />
					</Grid>
				)}
			</Grid>
		</Paper>
	)
}
