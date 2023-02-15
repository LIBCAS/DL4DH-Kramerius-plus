import { Divider, Grid, Paper, Typography } from '@mui/material'
import { getPublication, publish, unpublish } from 'api/publication-api'
import { Loading } from 'components/loading'
import { Publication } from 'models'
import { PublicationChildrenGrid } from 'modules/publications/publication-children-grid'
import { PublicationDetailInfo } from 'modules/publications/publication-detail-info'
import { PageWrapper } from 'pages/page-wrapper'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'

export const PublicationDetailPage: FC = () => {
	const [publication, setPublication] = useState<Publication>()
	const { publicationId } = useParams()

	useEffect(() => {
		async function fetchPublication() {
			if (publicationId) {
				const publication = await getPublication(publicationId)
				setPublication(publication)
			}
		}

		fetchPublication()
	}, [publicationId])

	const handlePublishButton = async () => {
		if (publicationId) {
			if (publication?.publishInfo.isPublished) {
				await unpublish(publicationId)
			} else {
				await publish(publicationId)
			}

			const fetchPublication = async () => {
				const publication = await getPublication(publicationId)
				setPublication(publication)
			}

			fetchPublication()
		}
	}

	return (
		<PageWrapper requireAuth>
			{publication ? (
				<Paper sx={{ p: 2 }}>
					<Grid container>
						<Grid item xs={12}>
							<PublicationDetailInfo
								handlePublish={handlePublishButton}
								publication={publication}
							/>
						</Grid>
						<Grid item xs={12}>
							<Divider sx={{ m: 2 }} />
						</Grid>
						<Grid container item spacing={2} sx={{ p: 2 }} xs={12}>
							<Grid item xs={12}>
								<Typography variant="h6">Podřazené publikace</Typography>
							</Grid>
							<Grid item xs={12}>
								<PublicationChildrenGrid parentId={publication.id} />
							</Grid>
						</Grid>
					</Grid>
				</Paper>
			) : (
				<Loading />
			)}
		</PageWrapper>
	)
}
