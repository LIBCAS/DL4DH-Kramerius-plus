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
import { useEffect, useState } from 'react'
import { PublicationExportDialog } from '../../components/publication/publication-export-dialog'
import { getPublication, publish, unpublish } from '../../api/publication-api'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { formatDateTime } from 'utils/formatters'
import { Loading } from 'components/loading'
import { useInfo } from 'components/navbar/info/info-context'

type Props = {
	publicationId: string
}

export const PublicationDetail = ({ publicationId }: Props) => {
	const [publication, setPublication] = useState<Publication>()
	const [selectedJobType, setSelectedJobType] =
		useState<EnrichmentKrameriusJob>()
	const [open, setOpen] = useState<boolean>(false)
	const { info } = useInfo()

	const onDialogOpen = () => {
		setOpen(true)
	}

	const onDialogClose = () => {
		setOpen(false)
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

	const handleJobTypeClick = (_: React.MouseEvent<HTMLElement>, value: any) => {
		setSelectedJobType(value)
	}

	const horizontalButtonGroups = useMediaQuery('(min-width:901px)', {
		noSsr: true,
	})

	const openInKramerius = () => {
		if (
			publication?.model === 'periodical' ||
			publication?.model === 'periodicalvolume'
		) {
			window.open(`${info?.kramerius.url}/periodical/${publicationId}`)
		} else {
			window.open(`${info?.kramerius.url}/view/${publicationId}`)
		}
	}

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
			<PublicationExportDialog
				open={open}
				publicationId={publicationId}
				onClose={onDialogClose}
			/>
		</Paper>
	) : (
		<Loading />
	)
}
