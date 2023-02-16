import { Box, Button, Grid, Paper, TextField, Typography } from '@mui/material'
import { EnrichmentRequestFilterDto } from 'pages/enrichment/enrichment-request-list'
import { ChangeEvent, FC, useState } from 'react'

type Props = {
	onSubmit: (filter: EnrichmentRequestFilterDto) => void
}

export const EnrichmentRequestFilter: FC<Props> = ({ onSubmit }) => {
	const [filter, setFilter] = useState<EnrichmentRequestFilterDto>(
		{} as EnrichmentRequestFilterDto,
	)

	const handleFieldChange =
		(field: string) => (event: ChangeEvent<HTMLInputElement>) => {
			setFilter(prevFilter => ({
				...prevFilter,
				[field]: event.target.value,
			}))
		}

	const handleSubmit = () => {
		onSubmit(filter)
	}

	return (
		<Paper elevation={2}>
			<Box component="form" sx={{ p: 2 }}>
				<Box sx={{ pb: 3 }}>
					<Typography variant="h5">Filtrování</Typography>
				</Box>
				<Grid container spacing={3}>
					<Grid item lg={3} xs={12}>
						<TextField
							fullWidth
							label="UUID Publikace"
							size="small"
							value={filter.publicationId}
							onChange={handleFieldChange('publicationId')}
						/>
					</Grid>
					<Grid item lg={3} xs={12}>
						<TextField
							fullWidth
							label="Název"
							size="small"
							value={filter.name}
							onChange={handleFieldChange('name')}
						/>
					</Grid>
					<Grid item lg={3} xs={12}>
						<TextField
							fullWidth
							label="Vytvořil"
							size="small"
							value={filter.owner}
							onChange={handleFieldChange('owner')}
						/>
					</Grid>
				</Grid>
				<Box sx={{ paddingTop: 3 }}>
					<Button color="primary" variant="contained" onClick={handleSubmit}>
						Filtrovat
					</Button>
				</Box>
			</Box>
		</Paper>
	)
}
