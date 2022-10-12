// import Grid from '@material-ui/core/Grid'
// import Typography from '@material-ui/core/Typography'

import { Grid, Typography } from '@mui/material'

type Props = {
	label: string
	value?: any
}

export const ReadOnlyField = ({ label, value }: Props) => {
	if (!value) {
		return <></>
	}

	return (
		<Grid container sx={{ m: 1 }}>
			<Grid item xs={2}>
				<Typography variant="body2">{label}</Typography>
			</Grid>
			<Grid item xs={10}>
				<Typography color="primary" variant="body2">
					{value}
				</Typography>
			</Grid>
		</Grid>
	)
}
