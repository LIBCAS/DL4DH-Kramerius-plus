import {
	makeStyles,
	Paper,
	Button,
	ButtonGroup,
	Typography,
} from '@material-ui/core'
import { Box } from '@mui/system'
import { GridRowParams } from '@mui/x-data-grid'
import { ReadOnlyField } from 'components/read-only-field/read-only-field'
import { Publication } from 'models'
import { JobType } from 'models/job-type'
import { KrameriusJob } from 'models/kramerius-job'
import {
	downloadKStructure,
	enrichExternal,
	enrichNdk,
	enrichTei,
} from 'modules/enrichment/enrichment-api'
import { JobEventList } from 'modules/jobs/job-event/job-event-list'
import { useContext, useState } from 'react'
import { useHistory } from 'react-router'
import { toast } from 'react-toastify'
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
	const [selectedJobType, setSelectedJobType] = useState<KrameriusJob>()
	const { replace } = useHistory()
	const [lastRender, setLastRender] = useState<number>(Date.now())

	const handleOpenExportDialog = () => {
		open({
			initialValues: {
				id: publication.id,
			},
			Content: PublicationExportDialog,
			size: 'md',
		})
	}

	const onRowClick = (params: GridRowParams) => {
		replace(`/jobs/enriching/${params.row['id']}`)
	}

	const createNewJob = async () => {
		async function createJob() {
			switch (selectedJobType) {
				case KrameriusJob.ENRICHMENT_KRAMERIUS:
					return downloadKStructure([publication.id], true)
				case KrameriusJob.ENRICHMENT_EXTERNAL:
					return enrichExternal([publication.id])
				case KrameriusJob.ENRICHMENT_NDK:
					return enrichNdk([publication.id])
				case KrameriusJob.ENRICHMENT_TEI:
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
		selectedOnType: KrameriusJob,
	): 'contained' | 'outlined' {
		return selectedJobType == selectedOnType ? 'contained' : 'outlined'
	}

	return (
		<Paper>
			<Box display="flex" flexDirection="row">
				<Box sx={{ p: 2 }} width="90%">
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
							variant={getButtonVariant(KrameriusJob.ENRICHMENT_KRAMERIUS)}
							onClick={() =>
								setSelectedJobType(KrameriusJob.ENRICHMENT_KRAMERIUS)
							}
						>
							Stahování dat
						</Button>
						<Button
							variant={getButtonVariant(KrameriusJob.ENRICHMENT_EXTERNAL)}
							onClick={() =>
								setSelectedJobType(KrameriusJob.ENRICHMENT_EXTERNAL)
							}
						>
							Obohacení externími nástroji
						</Button>
						<Button
							variant={getButtonVariant(KrameriusJob.ENRICHMENT_NDK)}
							onClick={() => setSelectedJobType(KrameriusJob.ENRICHMENT_NDK)}
						>
							Obohacení NDK
						</Button>
						<Button
							variant={getButtonVariant(KrameriusJob.ENRICHMENT_TEI)}
							onClick={() => setSelectedJobType(KrameriusJob.ENRICHMENT_TEI)}
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
						<JobEventList
							key={lastRender}
							jobType={JobType.Enriching}
							krameriusJob={selectedJobType}
							publicationId={publication.id}
							onRowClick={onRowClick}
						/>
					</Box>
				)}
			</Box>
		</Paper>
	)
}
