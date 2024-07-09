import { Grid } from '@mui/material'
import { GridPaginationModel } from '@mui/x-data-grid'
import { useQuery } from '@tanstack/react-query'
import { list, UserRequestListFilter } from 'api/user-request-api'
import { CustomErrorComponent } from 'components/custom-error-component'
import { UserRequestGrid } from 'modules/user-request/user-request-grid'
import { UserRequestFilters } from 'modules/user-request/user-request-list-filter'
import { PageWrapper } from 'pages/page-wrapper'
import { useState } from 'react'
import { useSearchParams } from 'react-router-dom'

export const UserRequestListPage = () => {
	const [searchParams, setSearchParams] = useSearchParams()
	const [filter, setFilters] = useState<UserRequestListFilter>({})

	const onPaginationChange = ({ page, pageSize }: GridPaginationModel) => {
		setSearchParams({
			...(page !== 0 ? { page: `${page + 1}` } : {}),
			...(pageSize !== 10 ? { pageSize: `${pageSize}` } : {}),
		})
	}

	const getPage = () => {
		return parseInt(searchParams.get('page') ?? '1') - 1
	}

	const getPageSize = () => {
		return parseInt(searchParams.get('pageSize') ?? '10')
	}

	const { isLoading, error, data } = useQuery({
		queryKey: ['GET', '/api/user-requests', getPage(), getPageSize(), filter],
		queryFn: () => list(getPage(), getPageSize(), filter),
	})

	const onFilterSubmit = (filters: UserRequestListFilter) => {
		setFilters({ ...filters })
	}

	return (
		<PageWrapper requireAuth>
			{error ? (
				<CustomErrorComponent error={error} />
			) : (
				<Grid container direction="column" spacing={3}>
					<Grid item xs={12}>
						<UserRequestFilters doFilter={onFilterSubmit} />
					</Grid>
					<Grid item xs={12}>
						<UserRequestGrid
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
