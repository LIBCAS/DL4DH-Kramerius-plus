import { Grid } from '@mui/material'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import { listExportRequests } from 'api/export-api'
import { ExportRequestFilter } from 'modules/export-request/export-request-filter'
import { ExportRequestGrid } from 'modules/export-request/export-request-grid'
import { PageWrapper } from 'pages/page-wrapper'
import { FC, useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { CustomErrorComponent } from 'components/custom-error-component'
import { GridPaginationModel } from '@mui/x-data-grid'

export interface ExportRequestFilterDto {
	name?: string
	owner?: string
	isFinished?: boolean
}

export const ExportRequestListPage: FC = () => {
	const queryClient = useQueryClient()
	const [searchParams, setSearchParams] = useSearchParams()
	const [filter, setFilter] = useState<ExportRequestFilterDto>()
	const { status, isLoading, data, error, isPreviousData } = useQuery({
		queryKey: [
			'exportRequests',
			parseInt(searchParams.get('page') ?? '1') - 1,
			parseInt(searchParams.get('pageSize') ?? '10'),
			filter,
		],
		queryFn: () =>
			listExportRequests(
				parseInt(searchParams.get('page') ?? '1') - 1,
				parseInt(searchParams.get('pageSize') ?? '10'),
				filter,
			),
		keepPreviousData: true,
		staleTime: 5000,
	})

	const onFilterSubmit = (filter: ExportRequestFilterDto) => {
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
				queryKey: ['exportRequests', page + 1, pageSize],
				queryFn: () => listExportRequests(page + 1, pageSize, filter),
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
						<ExportRequestFilter onSubmit={onFilterSubmit} />
					</Grid>
					<Grid item xs={12}>
						<ExportRequestGrid
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
