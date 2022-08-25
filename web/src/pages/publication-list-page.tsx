import { Grid } from '@mui/material'
import { PublicationDetail } from 'modules/publications/publication-detail'
import { FC } from 'react'
import { useNavigate, useParams } from 'react-router'
import { PublicationList } from '../modules/publications/publication-list'
import { PageWrapper } from './page-wrapper'

export const PublicationsPage: FC = () => {
	const { publicationId } = useParams()
	const navigate = useNavigate()

	const handleClick = (publicationId: string) => {
		navigate(`${publicationId}`)
	}

	return (
		<PageWrapper requireAuth>
			<Grid container spacing={2}>
				<Grid item md={6} xs={12}>
					<PublicationList onRowClick={handleClick} />
				</Grid>
				{publicationId && (
					<Grid item md={6} xs={12}>
						<PublicationDetail publicationId={publicationId} />
					</Grid>
				)}
			</Grid>
		</PageWrapper>
	)
}
