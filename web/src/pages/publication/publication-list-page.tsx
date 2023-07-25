import { Grid } from '@mui/material'
import { GridPaginationModel, GridRowSelectionModel } from '@mui/x-data-grid'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import {
	listPublications,
	PublicationFilter,
	publish,
	publishMultiple,
	unpublish,
	unpublishMultiple,
} from 'api/publication-api'
import { CustomErrorComponent } from 'components/custom-error-component'
import { PublicationExportDialog } from 'components/publication/publication-export-dialog'
import { PublicationGrid } from 'modules/publications/publication-grid'
import { PublicationListFilter } from 'modules/publications/publication-list-filter'
import { PageWrapper } from 'pages/page-wrapper'
import { FC, MouseEvent, useCallback, useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { toast } from 'react-toastify'
import OpenInNewIcon from '@mui/icons-material/OpenInNew'
import ExitToAppIcon from '@mui/icons-material/ExitToApp'
import PublicIcon from '@mui/icons-material/Public'
import PublicOffIcon from '@mui/icons-material/PublicOff'

export const PublicationListPage: FC = () => {
	const queryClient = useQueryClient()
	const [searchParams, setSearchParams] = useSearchParams()
	const [filter, setFilter] = useState<PublicationFilter>({
		isRootEnrichment: true,
	})
	const { status, isLoading, data, error, isPreviousData, refetch } = useQuery({
		queryKey: [
			'publications',
			parseInt(searchParams.get('page') ?? '1') - 1,
			parseInt(searchParams.get('pageSize') ?? '10'),
			filter,
		],
		queryFn: () =>
			listPublications(
				parseInt(searchParams.get('page') ?? '1') - 1,
				parseInt(searchParams.get('pageSize') ?? '10'),
				filter,
			),
		keepPreviousData: true,
		staleTime: 5000,
	})

	const [selection, setSelection] = useState<GridRowSelectionModel>([])
	const [publicationsToExport, setPublicationsToExport] = useState<string[]>([])

	const getSelectedPublications = () => {
		return selection.map(row => row.toString())
	}

	const onFilterSubmit = (filter: PublicationFilter) => {
		setFilter({ ...filter })
	}

	useEffect(() => {
		const page = parseInt(searchParams.get('page') ?? '1') - 1
		const pageSize = parseInt(searchParams.get('pageSize') ?? '10')
		if (
			!isPreviousData &&
			data &&
			data.total > (data.page + 1) * data.pageSize
		) {
			queryClient.prefetchQuery({
				queryKey: ['publications', page + 1, pageSize],
				queryFn: () => listPublications(page + 1, pageSize, filter),
			})
		}
	}, [data, filter, isPreviousData, searchParams, queryClient])

	const onPaginationChange = ({ page, pageSize }: GridPaginationModel) => {
		setSearchParams({
			...(page !== 0 ? { page: `${page + 1}` } : {}),
			...(pageSize !== 10 ? { pageSize: `${pageSize}` } : {}),
		})
	}

	const onExportSingleClick = useCallback(
		(publicationId: string) => (e: MouseEvent) => {
			e.stopPropagation()
			setPublicationsToExport([publicationId])
		},
		[],
	)

	const onPublishSingleClick = useCallback(
		(publicationId: string) => {
			publish(publicationId)
				.then(response => {
					if (response.ok) {
						toast('Položka byla úspěšně publikována.', { type: 'success' })
					} else {
						toast('Při publikování došlo k chybě.', { type: 'error' })
					}
				})
				.then(() => refetch())
		},
		[refetch],
	)

	const onUnpublishSingleClick = useCallback(
		(publicationId: string) => {
			unpublish(publicationId)
				.then(response => {
					if (response.ok) {
						toast('Publikování bylo úspěšně zrušeno.', { type: 'success' })
					} else {
						toast('Při zrušení publikovování došlo k chybě.', { type: 'error' })
					}
				})
				.then(() => refetch())
		},
		[refetch],
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
			.then(() => refetch())
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
			.then(() => refetch())
	}

	const getPublishLabel = (id: string) => {
		return data?.items.find(publication => publication.id === id)?.publishInfo
			.isPublished
			? 'Zrušit publikování'
			: 'Publikovat'
	}

	const getPublishIcon = (id: string) => {
		return data?.items.find(publication => publication.id === id)?.publishInfo
			.isPublished ? (
			<PublicOffIcon />
		) : (
			<PublicIcon />
		)
	}

	const getPublishAction = (id: string) => (e: MouseEvent) => {
		e.stopPropagation()
		return data?.items.find(publication => publication.id === id)?.publishInfo
			.isPublished
			? onUnpublishSingleClick(id)
			: onPublishSingleClick(id)
	}

	return (
		<PageWrapper requireAuth>
			{status === 'error' ? (
				<CustomErrorComponent error={error} />
			) : (
				<Grid container spacing={3}>
					<Grid item xs={12}>
						<PublicationListFilter onSubmit={onFilterSubmit} />
					</Grid>
					<Grid item xs={12}>
						<PublicationGrid
							actions={[
								{
									getLabel: () => 'Detail',
									getHref: (id: string) => {
										return `/publications/${id}`
									},
									getIcon: () => <OpenInNewIcon />,
								},
								{
									getLabel: (id: string) => getPublishLabel(id),
									getOnClick: (id: string) => getPublishAction(id),
									getIcon: (id: string) => getPublishIcon(id),
								},
								{
									getLabel: () => 'Exportovat',
									getOnClick: onExportSingleClick,
									getIcon: () => <ExitToAppIcon />,
								},
							]}
							checkboxSelection={true}
							data={data}
							isLoading={isLoading}
							pagination={{
								page: parseInt(searchParams.get('page') ?? '1') - 1,
								pageSize: parseInt(searchParams.get('pageSize') ?? '10'),
							}}
							selection={selection}
							toolbarProps={{
								onClearSelection: () => setSelection([]),
								onExportClick: () =>
									setPublicationsToExport(getSelectedPublications()),
								onPublishClick: onPublishMultipleClick,
								onUnpublishClick: onUnpublishMultipleClick,
							}}
							onPaginationChange={onPaginationChange}
							onSelectionChange={setSelection}
						/>
					</Grid>
					<PublicationExportDialog
						open={!!publicationsToExport.length}
						publicationIds={publicationsToExport}
						onClose={() => setPublicationsToExport([])}
					/>
				</Grid>
			)}
		</PageWrapper>
	)
}
