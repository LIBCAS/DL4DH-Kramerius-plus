import { useEffect, useState } from 'react'
import { v4 } from 'uuid'
import Paper from '@material-ui/core/Paper'
import Typography from '@material-ui/core/Typography'
import { makeStyles } from '@material-ui/core/styles'

import { FileRef } from '../../models'
import { ExportItem } from '../../components/export/export-item'
import { getExportedPublications } from './export-api'

const useStyles = makeStyles(() => ({
	title: {
		marginBottom: 10,
	},
	paper: {
		padding: '10px 24px',
		minHeight: 140,
		maxHeight: 640,
		overflow: 'scroll',
	},
}))

export const ExportedPublications = () => {
	const classes = useStyles()
	const [exportedPublications, setExportedPublications] = useState<FileRef[]>(
		[],
	)

	useEffect(() => {
		async function fetchExports() {
			const result = await getExportedPublications()

			if (result.length > 0) {
				setExportedPublications(result)
			}
		}

		fetchExports()
	}, [])

	return (
		<Paper className={classes.paper}>
			<div className={classes.title}>
				<Typography color="primary" variant="h6">
					Vygenerované exporty
				</Typography>
			</div>
			<div>
				{exportedPublications.map(ep => (
					<ExportItem key={v4()} exportedPublication={ep} />
				))}
			</div>
		</Paper>
	)
}
