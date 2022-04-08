import { makeStyles, Paper } from '@material-ui/core'
import Button from '@material-ui/core/Button'
import { Box } from '@mui/system'
import { ReadOnlyField } from 'components/read-only-field/read-only-field'
import { Publication } from 'models'
import { useContext } from 'react'
import { DialogContext } from '../../components/dialog/dialog-context'
import { PublicationExportDialog } from '../../components/publication/publication-export-dialog'

type Props = {
	publication: Publication
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

export const PublicationDetail = ({ publication }: Props) => {
	const classes = useStyles()
	const { open } = useContext(DialogContext)

	const handleOpenExportDialog = () => {
		open({
			initialValues: {
				id: publication.id,
			},
			Content: PublicationExportDialog,
			size: 'md',
		})
	}
	return (
		<Paper>
			<Box sx={{ p: 5 }}>
				<ReadOnlyField label="Název" value={publication.title} />
				<Box>
					<Button
						className={classes.exportButton}
						color="primary"
						variant="contained"
						onClick={handleOpenExportDialog}
					>
						Exportovat
					</Button>
				</Box>
			</Box>
		</Paper>
	)
}
