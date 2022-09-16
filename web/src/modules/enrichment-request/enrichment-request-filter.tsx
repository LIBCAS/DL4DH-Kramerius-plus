import { Box, Button, Grid, Paper, TextField, Typography } from '@mui/material'
import { EnrichmentRequestFilterDto } from 'models/enrichment-request/enrichment-request-filter-dto'
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
				<Grid container spacing={3}>
					<Grid item xs={12}>
						<Typography variant="h5">Filtrování</Typography>
					</Grid>
					<Grid item lg={3} md={5} sm={7} xs={9}>
						<TextField
							fullWidth
							label="Název"
							size="small"
							value={filter.name}
							onChange={handleFieldChange('name')}
						/>
					</Grid>
					<Grid item lg={3} md={5} sm={7} xs={9}>
						<TextField
							fullWidth
							label="Vytvořil"
							size="small"
							value={filter.owner}
							onChange={handleFieldChange('owner')}
						/>
					</Grid>
				</Grid>
				<Box sx={{ paddingTop: 4 }}>
					<Button color="primary" variant="contained" onClick={handleSubmit}>
						Filtrovat
					</Button>
				</Box>
			</Box>
		</Paper>
	)
}
