import {
	makeStyles,
	Paper,
	Button,
	ButtonGroup,
	Typography,
} from '@material-ui/core'
import { Box } from '@mui/system'
import { PublicationJobEventList } from 'components/publication/publication-job-event-list'
import { ReadOnlyField } from 'components/read-only-field/read-only-field'
import { EnrichmentKrameriusJob } from 'enums/enrichment-kramerius-job'
import { Publication } from 'models'
import {
	downloadKStructure,
	enrichExternal,
	enrichNdk,
	enrichTei,
} from 'api/enrichment-api'
import { useContext, useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import { DialogContext } from '../../components/dialog/dialog-context'
import { PublicationExportDialog } from '../../components/publication/publication-export-dialog'
import { publish, unpublish } from './publication-api'

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
	const [selectedJobType, setSelectedJobType] =
		useState<EnrichmentKrameriusJob>()
	const [lastRender, setLastRender] = useState<number>(Date.now())
	const [isPublished, setIsPublished] = useState<boolean>(
		publication.publishInfo.isPublished,
	)

	useEffect(() => {
		setIsPublished(publication.publishInfo.isPublished)
	}, [publication])

	const handleOpenExportDialog = () => {
		open({
			initialValues: {
				id: publication.id,
			},
			Content: PublicationExportDialog,
			size: 'md',
		})
	}

	const handlePublishButton = async () => {
		if (isPublished) {
			const response = await unpublish(publication.id)
			if (response.ok) {
				setIsPublished(false)
			}
		} else {
			const response = await publish(publication.id)
			if (response.ok) {
				setIsPublished(true)
			}
		}
	}

	const createNewJob = async () => {
		async function createJob() {
			switch (selectedJobType) {
				case EnrichmentKrameriusJob.ENRICHMENT_KRAMERIUS:
					return downloadKStructure([publication.id], true)
				case EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL:
					return enrichExternal([publication.id])
				case EnrichmentKrameriusJob.ENRICHMENT_NDK:
					return enrichNdk([publication.id])
				case EnrichmentKrameriusJob.ENRICHMENT_TEI:
					return enrichTei([publication.id])
			}
		}
		const response = await createJob()
		if (response?.ok) {
			toast('Operace proběhla úspěšně', {
				type: 'success',
			})
		} else {
			toast('Při pokusu o obohacení nastala chyba.', {
				type: 'error',
			})
		}

		setLastRender(Date.now())
	}

	function getButtonVariant(
		selectedOnType: EnrichmentKrameriusJob,
	): 'contained' | 'outlined' {
		return selectedJobType == selectedOnType ? 'contained' : 'outlined'
	}

	return (
		<Paper>
			<Box display="flex" flexDirection="row">
				<Box sx={{ p: 2 }} width="70%">
					<ReadOnlyField label="UUID" value={publication.id} />
					<ReadOnlyField label="Název" value={publication.title} />
					<ReadOnlyField
						label="ModsMetadata"
						value={JSON.stringify(publication.modsMetadata)}
					/>
					<ReadOnlyField label="Model" value={publication.model} />
					<ReadOnlyField
						label="Context"
						value={JSON.stringify(publication.context)}
					/>
				</Box>
				<Box sx={{ p: 4 }}>
					<Box sx={{ p: 1 }}>
						<Button
							className={classes.exportButton}
							color="primary"
							variant="contained"
							onClick={handleOpenExportDialog}
						>
							Exportovat
						</Button>
					</Box>
					<Box sx={{ p: 1 }}>
						<Button
							className={classes.exportButton}
							color="primary"
							variant="contained"
							onClick={handlePublishButton}
						>
							{isPublished ? 'Zrušit publikování' : 'Publikovat'}
						</Button>
					</Box>
				</Box>
			</Box>
			<Box>
				<Box sx={{ paddingLeft: 3 }}>
					<Typography variant="h6">Úlohy</Typography>
				</Box>
				<Box
					alignItems="center"
					display="flex"
					flexDirection="column"
					justifyContent="center"
					sx={{ p: 2 }}
				>
					<ButtonGroup className={classes.exportButton} color="primary">
						<Button
							variant={getButtonVariant(
								EnrichmentKrameriusJob.ENRICHMENT_KRAMERIUS,
							)}
							onClick={() =>
								setSelectedJobType(EnrichmentKrameriusJob.ENRICHMENT_KRAMERIUS)
							}
						>
							Stahování dat
						</Button>
						<Button
							variant={getButtonVariant(
								EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL,
							)}
							onClick={() =>
								setSelectedJobType(EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL)
							}
						>
							Obohacení externími nástroji
						</Button>
						<Button
							variant={getButtonVariant(EnrichmentKrameriusJob.ENRICHMENT_NDK)}
							onClick={() =>
								setSelectedJobType(EnrichmentKrameriusJob.ENRICHMENT_NDK)
							}
						>
							Obohacení NDK
						</Button>
						<Button
							variant={getButtonVariant(EnrichmentKrameriusJob.ENRICHMENT_TEI)}
							onClick={() =>
								setSelectedJobType(EnrichmentKrameriusJob.ENRICHMENT_TEI)
							}
						>
							Obohacení TEI
						</Button>
					</ButtonGroup>
				</Box>
				<Box sx={{ p: 3 }}>
					<Button
						color="primary"
						disabled={selectedJobType == undefined}
						variant="contained"
						onClick={createNewJob}
					>
						Spustit novou úlohu
					</Button>
				</Box>
				{selectedJobType !== undefined && (
					<Box sx={{ p: 3 }}>
						<PublicationJobEventList
							key={lastRender}
							krameriusJob={selectedJobType}
							publicationId={publication.id}
						/>
					</Box>
				)}
			</Box>
		</Paper>
	)
}
