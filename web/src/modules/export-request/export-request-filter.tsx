import { Box, Button, Grid, Paper, TextField, Typography } from '@mui/material'
import { ExportRequestFilterDto } from 'pages/export/export-request-list'
import { ChangeEvent, FC, useState } from 'react'

type Props = {
	onSubmit: (filter: ExportRequestFilterDto) => void
}

export const ExportRequestFilter: FC<Props> = ({ onSubmit }) => {
	const [filter, setFilter] = useState<ExportRequestFilterDto>({})

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
				<Box sx={{ paddingTop: 3 }}>
					<Button color="primary" variant="contained" onClick={handleSubmit}>
						Filtrovat
					</Button>
				</Box>
			</Box>
		</Paper>
	)
}
