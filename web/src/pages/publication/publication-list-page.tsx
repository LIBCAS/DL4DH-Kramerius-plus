import { Grid } from '@mui/material'
import { GridPaginationModel, GridRowSelectionModel } from '@mui/x-data-grid'
import {
	listPublications,
	PublicationFilter,
	publish,
	publishMultiple,
	unpublishMultiple,
} from 'api/publication-api'
import { PublicationExportDialog } from 'components/publication/publication-export-dialog'
import { Publication } from 'models'
import { QueryResults } from 'models/query-results'
import { PublicationGrid } from 'modules/publications/publication-grid'
import { PublicationListFilter } from 'modules/publications/publication-list-filter'
import { PageWrapper } from 'pages/page-wrapper'
import { FC, useCallback, useEffect, useState } from 'react'
import { toast } from 'react-toastify'

export const PublicationListPage: FC = () => {
	const [filter, setFilter] = useState<PublicationFilter>({
		isRootEnrichment: true,
	})
	const [data, setData] = useState<QueryResults<Publication>>()

	const [selection, setSelection] = useState<GridRowSelectionModel>([])
	const [pagination, setPagination] = useState<GridPaginationModel>({
		page: 0,
		pageSize: 10,
	})
	const [publicationsToExport, setPublicationsToExport] = useState<string[]>([])

	const getSelectedPublications = () => {
		return selection.map(row => row.toString())
	}

	const onFilterSubmit = (filter: PublicationFilter) => {
		setFilter({ ...filter })
	}

	const exportSelected = () => {
		setPublicationsToExport(getSelectedPublications())
	}

	const clearSelected = () => {
		setSelection([])
	}

	const fetchPublications = useCallback(
		async (pagination: GridPaginationModel) => {
			const response = await listPublications(
				pagination.page,
				pagination.pageSize,
				filter,
			)

			if (response) {
				setData(response)
			}
		},
		[filter],
	)

	useEffect(() => {
		fetchPublications(pagination)
	}, [fetchPublications, pagination])

	const onExportSingleClick = useCallback(
		(publicationId: string) => (e: React.MouseEvent) => {
			e.stopPropagation()

			setPublicationsToExport([publicationId])
		},
		[],
	)

	const onPublishSingleClick = useCallback(
		(publicationId: string) => async (e: React.MouseEvent) => {
			e.stopPropagation()

			const response = await publish(publicationId)

			if (response.ok) {
				setData(prev => {
					if (prev) {
						const items = prev.items.map(pub => {
							if (pub.id === publicationId) {
								return {
									...pub,
									publishInfo: { ...pub.publishInfo, isPublished: true },
								}
							} else {
								return pub
							}
						})

						return { ...prev, items }
					}

					return prev
				})

				toast('Položka byla úspěšně publikována.', { type: 'success' })
			} else {
				toast('Při publikování došlo k chybě.', { type: 'error' })
			}
		},
		[],
	)

	const onPublishMultipleClick = () => {
		publishMultiple(getSelectedPublications())
			.then(response => {
				if (response.ok) {
					toast('Položky byly úspěšně publikovány.', { type: 'success' })
				} else {
					toast('Při publikování došlo k chybě.', { type: 'error' })
				}
			})
			.then(() => fetchPublications(pagination))
	}

	const onUnpublishMultipleClick = () => {
		unpublishMultiple(getSelectedPublications())
			.then(response => {
				if (response.ok) {
					toast('Zrušení publikování položek bylo úspěšné.', {
						type: 'success',
					})
				} else {
					toast('Při zrušení publikování došlo k chybě.', { type: 'error' })
				}
			})
			.then(() => fetchPublications(pagination))
	}

	return (
		<PageWrapper requireAuth>
			<Grid container spacing={3}>
				<Grid item xs={12}>
					<PublicationListFilter onSubmit={onFilterSubmit} />
				</Grid>
				<Grid item xs={12}>
					<PublicationGrid
						gridProps={{
							data,
							checkboxSelection: true,
							pagination,
							onPaginationChange: setPagination,
							selection,
							onSelectionChange: setSelection,
							toolbarProps: {
								onClearSelection: clearSelected,
								onExportClick: exportSelected,
								onPublishClick: onPublishMultipleClick,
								onUnpublishClick: onUnpublishMultipleClick,
							},
						}}
						onExportClick={onExportSingleClick}
						onPublishClick={onPublishSingleClick}
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
