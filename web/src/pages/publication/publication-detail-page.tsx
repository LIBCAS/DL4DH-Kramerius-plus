import { Divider, Grid, Paper, Typography } from '@mui/material'
import { GridCallbackDetails, GridSelectionModel } from '@mui/x-data-grid'
import { getPublication, publish, unpublish } from 'api/publication-api'
import { Loading } from 'components/loading'
import { PublicationDetailJobs } from 'components/publication/publication-detail-jobs'
import { KrameriusJob } from 'enums/kramerius-job'
import { Publication } from 'models'
import { PublicationDetail } from 'modules/publications/publication-detail-new'
import { PublicationGrid } from 'modules/publications/publication-grid'
import { PageWrapper } from 'pages/page-wrapper'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'

export const PublicationDetailPage: FC = () => {
	const [publication, setPublication] = useState<Publication>()
	const [selectedJobType, setSelectedJobType] = useState<KrameriusJob>()
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

	const handleJobTypeClick = (_: React.MouseEvent<HTMLElement>, value: any) => {
		setSelectedJobType(value)
	}

	return (
		<PageWrapper requireAuth>
			{publication ? (
				<Paper sx={{ p: 2 }}>
					<Grid container>
						<Grid item xs={12}>
							<PublicationDetail
								handlePublish={handlePublishButton}
								publication={publication}
							/>
						</Grid>
						<Grid item xs={12}>
							<Divider sx={{ m: 2 }} />
						</Grid>
						<Grid item xs={12}>
							<PublicationDetailJobs
								handleJobTypeClick={handleJobTypeClick}
								publication={publication}
								selectedJobType={selectedJobType}
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
								<PublicationGrid
									filter={{ parentId: publication.id }}
									onSelectionChange={(
										// eslint-disable-next-line @typescript-eslint/no-unused-vars
										_s: GridSelectionModel,
										// eslint-disable-next-line @typescript-eslint/no-unused-vars
										_d: GridCallbackDetails,
									) => {}}
								/>
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
