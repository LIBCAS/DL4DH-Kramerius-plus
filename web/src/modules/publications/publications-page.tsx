import { Box } from '@mui/system'
import { PublicationDetail } from 'modules/publications/publication-detail'
import { Publication } from 'models'
import { useState } from 'react'
import { PublicationList } from './publication-list'

export const PublicationsPage = () => {
	const [selectedPublication, setSelectedPublication] = useState<Publication>()

	return (
		<Box display="flex" flexDirection="row">
			<Box sx={{ m: 2 }} width="55%">
				<PublicationList onRowClick={setSelectedPublication} />
			</Box>
			{selectedPublication && (
				<Box sx={{ m: 2 }} width="45%">
					<PublicationDetail publication={selectedPublication} />
				</Box>
			)}
		</Box>
	)
}
