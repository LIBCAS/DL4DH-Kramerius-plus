import {
	Grid,
	Paper,
	ToggleButtonGroup,
	ToggleButton,
	useMediaQuery,
	Button,
	Typography,
} from '@mui/material'
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
import { getPublication, publish, unpublish } from '../../api/publication-api'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { formatDateTime } from 'utils/formatters'
import { Loading } from 'components/loading'

type Props = {
	publicationId: string
}

export const PublicationDetail = ({ publicationId }: Props) => {
	const [publication, setPublication] = useState<Publication>()
	const { open } = useContext(DialogContext)
	const [selectedJobType, setSelectedJobType] =
		useState<EnrichmentKrameriusJob>()

	const handleOpenExportDialog = () => {
		open({
			initialValues: {
				id: publicationId,
			},
			Content: PublicationExportDialog,
			size: 'md',
		})
	}

	useEffect(() => {
		const fetchPublication = async () => {
			const publication = await getPublication(publicationId)
			setPublication(publication)
			setSelectedJobType(undefined)
		}

		fetchPublication()
	}, [publicationId])

	const handlePublishButton = async () => {
		if (publication?.publishInfo.isPublished) {
			await unpublish(publicationId)
		} else {
			await publish(publicationId)
		}

		const fetchPublication = async () => {
			const publication = await getPublication(publicationId)
			setPublication(publication)
			setSelectedJobType(undefined)
		}

		fetchPublication()
	}

	const createNewJob = async () => {
		async function createJob() {
			switch (selectedJobType) {
				case EnrichmentKrameriusJob.ENRICHMENT_KRAMERIUS:
					return downloadKStructure([publicationId], true)
				case EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL:
					return enrichExternal([publicationId])
				case EnrichmentKrameriusJob.ENRICHMENT_NDK:
					return enrichNdk([publicationId])
				case EnrichmentKrameriusJob.ENRICHMENT_TEI:
					return enrichTei([publicationId])
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

		const fetchPublication = async () => {
			const publication = await getPublication(publicationId)
			setPublication(publication)
			setSelectedJobType(undefined)
		}

		fetchPublication()
	}

	const handleJobTypeClick = (_: React.MouseEvent<HTMLElement>, value: any) => {
		setSelectedJobType(value)
	}

	const horizontalButtonGroups = useMediaQuery('(min-width:901px)', {
		noSsr: true,
	})

	return publication ? (
		<Paper sx={{ p: 3 }} variant="outlined">
			<Grid container spacing={2}>
				<Grid container item justifyContent="space-between" spacing={1}>
					<Grid item xl={8} xs={12}>
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
						<ReadOnlyField label="Model" value={publication.model} />
						<ReadOnlyField
							label="Context"
							value={JSON.stringify(publication.context)}
						/>
					</Grid>
					<Grid container direction="column" item spacing={1} xl={4} xs={12}>
						<Grid item xs={1}>
							<Button
								color="primary"
								fullWidth
								variant="contained"
								onClick={handleOpenExportDialog}
							>
								Exportovat
							</Button>
						</Grid>
						<Grid item xs={1}>
							<Button
								color="primary"
								fullWidth
								variant="contained"
								onClick={handlePublishButton}
							>
								{publication.publishInfo.isPublished
									? 'Zrušit publikování'
									: 'Publikovat'}
							</Button>
						</Grid>
					</Grid>
				</Grid>
				<Grid container item spacing={2} xs={12}>
					<Grid
						container
						display="flex"
						item
						justifyContent="space-between"
						xs={12}
					>
						<Grid item lg={4} xl={8}>
							<Typography variant="h6">Úlohy</Typography>
						</Grid>
						<Grid item lg={8} xl={4}>
							<Button
								color="primary"
								disabled={!selectedJobType}
								fullWidth
								variant="contained"
								onClick={createNewJob}
							>
								Spustit novou úlohu
							</Button>
						</Grid>
					</Grid>
					<Grid item xs={12}>
						<ToggleButtonGroup
							exclusive
							fullWidth
							orientation={`${
								horizontalButtonGroups ? `horizontal` : `vertical`
							}`}
							size="small"
							value={selectedJobType}
							onChange={handleJobTypeClick}
						>
							{Object.values(EnrichmentKrameriusJob).map(jobType => (
								<ToggleButton
									key={jobType.toString()}
									color="primary"
									value={jobType as EnrichmentKrameriusJob}
								>
									{KrameriusJobMapping[jobType]}
								</ToggleButton>
							))}
						</ToggleButtonGroup>
					</Grid>
					<Grid item xs={12}>
						{selectedJobType !== undefined && (
							<PublicationJobEventList
								krameriusJob={selectedJobType}
								publicationId={publicationId}
							/>
						)}
					</Grid>
				</Grid>
			</Grid>
		</Paper>
	) : (
		<Loading />
	)
}
