import { Box } from '@mui/system'
import { PublicationDetail } from 'modules/publications/publication-detail'
import { Publication } from 'models'
import { useEffect, useState } from 'react'
import { PublicationList } from './publication-list'
import { useHistory, useParams } from 'react-router'
import { getPublication } from './publication-api'
import { GridRowParams } from '@mui/x-data-grid'

export const PublicationsPage = () => {
	const [selectedPublication, setSelectedPublication] = useState<Publication>()
	const { publicationId } = useParams<{ publicationId: string }>()
	const { replace } = useHistory()

	async function fetchPublicationDetail(publicationId: string) {
		const publication = await getPublication(publicationId)
		setSelectedPublication(publication)
	}

	useEffect(() => {
		if (publicationId) {
			fetchPublicationDetail(publicationId)
		}
	}, [publicationId])

	const handleClick = (params: GridRowParams) => {
		replace(`/publications/${params.row['id']}`)
	}

	return (
		<Box display="flex" flexDirection="row">
			<Box paddingRight={2} width="40%">
				<PublicationList onRowClick={handleClick} />
			</Box>
			{selectedPublication && (
				<Box width="60%">
					<PublicationDetail publication={selectedPublication} />
				</Box>
			)}
		</Box>
	)
}
