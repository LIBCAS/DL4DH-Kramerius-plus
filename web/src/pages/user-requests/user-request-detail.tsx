import {
	Divider,
	Grid,
	Paper,
	ToggleButton,
	ToggleButtonGroup,
	Typography,
} from '@mui/material'
import { useQuery } from '@tanstack/react-query'
import { getById } from 'api/user-request-api'
import { CustomErrorComponent } from 'components/custom-error-component'
import { Loading } from 'components/loading'
import { UserRequestDetailDto } from 'models/user-requests'
import { UserRequestActions } from 'modules/user-request/user-request-actions'
import { UserRequestDetailInfo } from 'modules/user-request/user-request-detail-info'
import { UserRequestItems } from 'modules/user-request/user-request-items'
import { UserRequestMessages } from 'modules/user-request/user-request-messages'
import { UserRequestStateChanges } from 'modules/user-request/user-request-state-changes'
import { PageWrapper } from 'pages/page-wrapper'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'

export const UserRequestDetailPage: FC = () => {
	const { id } = useParams()
	const [request, setRequest] = useState<UserRequestDetailDto>()
	const [view, setView] = useState<string>('items')

	const { data, isLoading, error } = useQuery({
		queryKey: ['GET', '/api/user-requests', id],
		queryFn: async () => {
			if (!id) {
				throw new Error('Request ID is missing')
			}
			return await getById(id)
		},
	})

	useEffect(() => {
		if (data) {
			setRequest(data)
		}
	}, [data, isLoading, error, id])

	if (isLoading) {
		return <Loading />
	}

	return (
		<PageWrapper requireAuth>
			{request ? (
				<Grid container>
					<Grid item xs={12}>
						<Paper sx={{ p: 2 }}>
							<Grid container>
								<Typography variant="h6">Detail žádosti</Typography>
								<Grid item xs={12}>
									<Divider sx={{ my: 2 }} />
								</Grid>
								<Grid item xs={6}>
									<UserRequestDetailInfo request={request} />
								</Grid>
								<Grid item xs={6}>
									<UserRequestActions request={request} />
								</Grid>
							</Grid>
						</Paper>
					</Grid>
					<Grid item sx={{ marginTop: 2 }} xs={12}>
						<ToggleButtonGroup
							color="primary"
							exclusive
							fullWidth
							size="small"
							value={view}
							onChange={(_, val) => setView(val)}
						>
							<ToggleButton value={'items'}>Položky žádosti</ToggleButton>
							<ToggleButton value={'messages'}>Správy žádosti</ToggleButton>
							<ToggleButton value={'history'}>Historie změn</ToggleButton>
						</ToggleButtonGroup>
					</Grid>
					<Grid item xs={12}>
						{view === 'items' && (
							<UserRequestItems items={request.parts} requestId={request.id} />
						)}
						{view === 'messages' && (
							<UserRequestMessages
								messages={request.messages}
								requestId={request.id}
							/>
						)}
						{view === 'history' && (
							<UserRequestStateChanges stateChanges={request.stateChanges} />
						)}
					</Grid>
				</Grid>
			) : (
				<CustomErrorComponent error={error} />
			)}
		</PageWrapper>
	)
}

export const getView = (view: string) => {
	switch (view) {
	}
}
