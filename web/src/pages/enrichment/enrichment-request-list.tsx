import { Grid } from '@mui/material'
import { GridPaginationModel } from '@mui/x-data-grid'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import { listEnrichmentRequests } from 'api/enrichment-api'
import { CustomErrorComponent } from 'components/custom-error-component'
import { EnrichmentRequestFilter } from 'modules/enrichment-request/enrichment-request-filter'
import { EnrichmentRequestGrid } from 'modules/enrichment-request/enrichment-request-grid'
import { FC, useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { PageWrapper } from '../page-wrapper'
import { RequestState } from '../../models/request/request'

export type EnrichmentRequestFilterDto = {
	name?: string
	owner?: string
	isFinished?: boolean
	publicationId?: string
	state?: RequestState | ''
}

export const EnrichmentRequestList: FC = () => {
	const queryClient = useQueryClient()
	const [searchParams, setSearchParams] = useSearchParams()
	const [filter, setFilter] = useState<EnrichmentRequestFilterDto>({})
	const { status, isLoading, data, error, isPreviousData } = useQuery({
		queryKey: [
			'enrichmentRequests',
			parseInt(searchParams.get('page') ?? '1') - 1,
			parseInt(searchParams.get('pageSize') ?? '10'),
			filter,
		],
		queryFn: () =>
			listEnrichmentRequests(
				parseInt(searchParams.get('page') ?? '1') - 1,
				parseInt(searchParams.get('pageSize') ?? '10'),
				filter,
			),
		keepPreviousData: true,
		staleTime: 5000,
	})

	const onFilterSubmit = (filter: EnrichmentRequestFilterDto) => {
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
				queryKey: ['enrichmentRequests', page + 1, pageSize],
				queryFn: () => listEnrichmentRequests(page + 1, pageSize, filter),
			})
		}
	}, [data, filter, isPreviousData, searchParams, queryClient])

	const onPaginationChange = ({ page, pageSize }: GridPaginationModel) => {
		setSearchParams({
			...(page !== 0 ? { page: `${page + 1}` } : {}),
			...(pageSize !== 10 ? { pageSize: `${pageSize}` } : {}),
		})
	}

	return (
		<PageWrapper requireAuth>
			{status === 'error' ? (
				<CustomErrorComponent error={error} />
			) : (
				<Grid container direction="column" spacing={3}>
					<Grid item xs={12}>
						<EnrichmentRequestFilter onSubmit={onFilterSubmit} />
					</Grid>
					<Grid item xs={12}>
						<EnrichmentRequestGrid
							data={data}
							isLoading={isLoading}
							pagination={{
								page: parseInt(searchParams.get('page') ?? '1') - 1,
								pageSize: parseInt(searchParams.get('pageSize') ?? '10'),
							}}
							onPaginationChange={onPaginationChange}
						/>
					</Grid>
				</Grid>
			)}
		</PageWrapper>
	)
}
