import { Grid } from '@mui/material'
import { PublicationDetail } from 'modules/publications/publication-detail'
import { useState } from 'react'
import { PublicationList } from '../modules/publications/publication-list'

export const PublicationsPage = () => {
	const [selectedPublication, setSelectedPublication] = useState<string>()

	const handleClick = (publicationId: string) => {
		setSelectedPublication(publicationId)
	}

	return (
		<Grid container spacing={2}>
			<Grid item md={6} xs={12}>
				<PublicationList onRowClick={handleClick} />
			</Grid>
			{selectedPublication && (
				<Grid item md={6} xs={12}>
					<PublicationDetail publicationId={selectedPublication} />
				</Grid>
			)}
		</Grid>
	)
}
