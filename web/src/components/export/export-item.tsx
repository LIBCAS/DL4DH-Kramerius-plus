import Paper from '@material-ui/core/Paper'
import Grid from '@material-ui/core/Grid'
import Button from '@material-ui/core/Button'
import { makeStyles } from '@material-ui/core/styles'

import { Export } from '../../models'
import { ReadOnlyField } from '../read-only-field/read-only-field'

type Props = {
	exportedPublication: Export
}

const useStyles = makeStyles(() => ({
	paper: {
		marginBottom: 8,
		padding: '8px 16px',
	},
	exportButton: {
		textTransform: 'none',
		padding: '6px 10px',
	},
}))

export const ExportItem = ({ exportedPublication }: Props) => {
	const classes = useStyles()

	const { id, publicationId, publicationTitle, created, deleted, fileRef } =
		exportedPublication

	const sizeMB = `${((fileRef?.size ?? 0) / 1048576).toFixed(2)} MB`

	const localizedCreation = created
		? new Date(created).toLocaleString('cs')
		: undefined

	const localizedDeletion = deleted
		? new Date(deleted).toLocaleString('cs')
		: undefined

	const handleDownloadExport = () => {
		window.open(
			process.env.PUBLIC_URL + `/api/export/download/${fileRef?.id}`,
			'_blank',
		)
	}

	return (
		<Paper className={classes.paper}>
			<Grid alignItems="center" container justifyContent="center">
				<Grid item xs={10}>
					<ReadOnlyField label="Id:" value={id} />
					<ReadOnlyField label="Publikace:" value={publicationTitle} />
					<ReadOnlyField label="Id publikace:" value={publicationId} />
					<ReadOnlyField label="Vytvoření:" value={localizedCreation} />
					{deleted && (
						<ReadOnlyField label="Zmazáno" value={localizedDeletion} />
					)}
					<ReadOnlyField label="Název souboru:" value={fileRef?.name} />
					<ReadOnlyField label="Typ" value={fileRef?.contentType} />
					<ReadOnlyField label="Velikosť:" value={sizeMB} />
				</Grid>
				<Grid item xs={2}>
					<Button
						className={classes.exportButton}
						color="primary"
						variant="contained"
						onClick={handleDownloadExport}
					>
						Stáhnout
					</Button>
				</Grid>
			</Grid>
		</Paper>
	)
}
