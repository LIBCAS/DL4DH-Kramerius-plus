import { Grid } from '@mui/material'
import { PublicationFilter } from 'api/publication-api'
import { PublicationExportDialog } from 'components/publication/publication-export-dialog'
import { PublicationListFilter } from 'modules/publications/publication-list-filter'
import { PublicationGrid } from 'modules/publications/publication-grid'
import { PageWrapper } from 'pages/page-wrapper'
import { FC, useState } from 'react'
import { GridSelectionModel } from '@mui/x-data-grid'

export const PublicationListPage: FC = () => {
	const [filter, setFilter] = useState<PublicationFilter>({
		isRootEnrichment: true,
	})
	const [open, setOpen] = useState<boolean>(false)
	const [selectedPublications, setSelectedPublications] = useState<string[]>([])

	const onFilterSubmit = (filter: PublicationFilter) => {
		setFilter({ ...filter })
	}

	const onDialogOpen = () => {
		setOpen(true)
	}

	const onDialogClose = () => {
		setOpen(false)
	}

	const onSelectionChange = (selectionModel: GridSelectionModel) => {
		setSelectedPublications(selectionModel.map(model => model.toString()))
	}

	return (
		<PageWrapper requireAuth>
			<Grid container spacing={3}>
				<Grid item xs={12}>
					<PublicationListFilter
						onExportClick={onDialogOpen}
						onSubmit={onFilterSubmit}
					/>
				</Grid>
				<Grid item xs={12}>
					<PublicationGrid
						filter={filter}
						onSelectionChange={onSelectionChange}
					/>
				</Grid>
				<PublicationExportDialog
					open={open}
					publicationIds={selectedPublications}
					onClose={onDialogClose}
				/>
			</Grid>
		</PageWrapper>
	)
}
