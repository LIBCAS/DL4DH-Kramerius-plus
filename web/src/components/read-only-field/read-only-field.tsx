import Grid from '@material-ui/core/Grid'
import Typography from '@material-ui/core/Typography'

type Props = {
	label: string
	value?: any
}

export const ReadOnlyField = ({ label, value }: Props) => {
	if (!value) {
		return <></>
	}

	return (
		<Grid container style={{ margin: 5, padding: 5 }}>
			<Grid item xs={3}>
				<Typography variant="body2">{label}</Typography>
			</Grid>
			<Grid item xs={9}>
				<Typography color="primary" variant="body2">
					{value}
				</Typography>
			</Grid>
		</Grid>
	)
}
