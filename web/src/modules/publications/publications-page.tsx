import { Box, spacing } from '@mui/system'
import { PublicationDetail } from 'components/publication/publication-detail'
import { PublicationList } from 'components/publication/publication-list'
import { Publication } from 'models'
import { useState } from 'react'

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
