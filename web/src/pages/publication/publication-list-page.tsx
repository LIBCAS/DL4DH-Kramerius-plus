import { Grid } from '@mui/material'
import { GridRowId, GridSelectionModel } from '@mui/x-data-grid'
import {
	PublicationFilter,
	publishMultiple,
	unpublishMultiple,
} from 'api/publication-api'
import { PublicationExportDialog } from 'components/publication/publication-export-dialog'
import { PublicationGrid } from 'modules/publications/publication-grid'
import { PublicationListFilter } from 'modules/publications/publication-list-filter'
import { PageWrapper } from 'pages/page-wrapper'
import { FC, useState } from 'react'

export const PublicationListPage: FC = () => {
	const [filter, setFilter] = useState<PublicationFilter>({
		isRootEnrichment: true,
	})
	// const [selectedPublications, setSelectedPublications] = useState<string[]>([])
	const [selectionModel, setSelectionModel] = useState<GridRowId[]>([])
	const [publicationsToExport, setPublicationsToExport] = useState<string[]>([])

	const getSelectedPublications = () => {
		return selectionModel.map(row => row.toString())
	}

	const onFilterSubmit = (filter: PublicationFilter) => {
		setFilter({ ...filter })
	}

	const onSelectionChange = (selectionModel: GridSelectionModel) => {
		setSelectionModel(selectionModel)
	}

	const onExportSingleClick = (publicationId: string) => {
		setPublicationsToExport([publicationId])
	}

	const exportSelected = () => {
		setPublicationsToExport(getSelectedPublications())
	}

	const publishSelected = async () => {
		return await publishMultiple(getSelectedPublications())
	}

	const unpublishSelected = async () => {
		return await unpublishMultiple(getSelectedPublications())
	}

	const clearSelected = () => {
		setSelectionModel([])
	}

	return (
		<PageWrapper requireAuth>
			<Grid container spacing={3}>
				<Grid item xs={12}>
					<PublicationListFilter onSubmit={onFilterSubmit} />
				</Grid>
				<Grid item xs={12}>
					<PublicationGrid
						filter={filter}
						publishMultiple={publishSelected}
						selectionModel={selectionModel}
						unpublishMultiple={unpublishSelected}
						onClearSelection={clearSelected}
						onExportMultipleClick={exportSelected}
						onExportSingleClick={onExportSingleClick}
						onSelectionChange={onSelectionChange}
					/>
				</Grid>
				<PublicationExportDialog
					open={!!publicationsToExport.length}
					publicationIds={publicationsToExport}
					onClose={() => setPublicationsToExport([])}
				/>
			</Grid>
		</PageWrapper>
	)
}
