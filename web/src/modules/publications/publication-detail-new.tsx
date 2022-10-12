import { Button, Grid } from '@mui/material'
import { useInfo } from 'components/navbar/info/info-context'
import { PublicationExportDialog } from 'components/publication/publication-export-dialog'
import { ReadOnlyField } from 'components/read-only-field/read-only-field'
import { DigitalObjectModelMapping } from 'enums/publication-model'
import { Publication } from 'models'
import { FC, useState } from 'react'
import { formatDateTime } from 'utils/formatters'

type Props = {
	publication: Publication
	handlePublish: () => void
}

export const PublicationDetail: FC<Props> = ({
	publication,
	handlePublish,
}) => {
	const [open, setOpen] = useState<boolean>(false)
	const { info } = useInfo()

	const onDialogOpen = () => {
		setOpen(true)
	}

	const onDialogClose = () => {
		setOpen(false)
	}

	const openInKramerius = () => {
		if (
			publication?.model === 'periodical' ||
			publication?.model === 'periodicalvolume'
		) {
			window.open(`${info?.kramerius.url}/periodical/${publication.id}`)
		} else {
			window.open(`${info?.kramerius.url}/view/${publication.id}`)
		}
	}

	return (
		<Grid container spacing={2} sx={{ p: 2 }}>
			<Grid container item spacing={4}>
				<Grid item rowSpacing={2} xl={10} xs={12}>
					<ReadOnlyField label="UUID" value={publication.id} />
					<ReadOnlyField
						label="Vytvořeno"
						value={
							publication && formatDateTime(publication.created.toString())
						}
					/>
					<ReadOnlyField label="Název" value={publication.title} />
					<ReadOnlyField
						label="ModsMetadata"
						value={JSON.stringify(publication.modsMetadata, null, 4)}
					/>
					<ReadOnlyField
						label="Model"
						value={
							publication.model
								? DigitalObjectModelMapping[publication.model]
								: ''
						}
					/>
					<ReadOnlyField
						label="Context"
						value={JSON.stringify(publication.context)}
					/>
				</Grid>
				<Grid container direction="column" item spacing={1} xl={2} xs={12}>
					<Grid item xs={1}>
						<Button
							color="primary"
							fullWidth
							variant="contained"
							onClick={openInKramerius}
						>
							Otevřít v Krameriovi
						</Button>
					</Grid>
					<Grid item xs={1}>
						<Button
							color="primary"
							fullWidth
							variant="contained"
							onClick={onDialogOpen}
						>
							Exportovat
						</Button>
					</Grid>
					<Grid item xs={1}>
						<Button
							color="primary"
							fullWidth
							variant="contained"
							onClick={handlePublish}
						>
							{publication.publishInfo.isPublished
								? 'Zrušit publikování'
								: 'Publikovat'}
						</Button>
					</Grid>
				</Grid>
			</Grid>
			<PublicationExportDialog
				open={open}
				publicationIds={[publication.id]}
				onClose={onDialogClose}
			/>
		</Grid>
	)
}
