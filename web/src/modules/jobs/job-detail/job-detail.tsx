import { Grid, makeStyles, Paper } from '@material-ui/core'
import { JobInstance } from 'models/job-instance'
import { JobInstanceDetail } from './job-instance-detail'

type Props = {
	job?: JobInstance
}

const useStyles = makeStyles(() => ({
	paper: {
		padding: '20px',
	},
}))

export const JobDetail = ({ job }: Props) => {
	const classes = useStyles()

	return (
		<Paper className={classes.paper}>
			<Grid container xs={12}>
				{job && (
					<Grid item style={{ width: '100%' }}>
						<JobInstanceDetail job={job} />
					</Grid>
				)}
			</Grid>
		</Paper>
	)
}
