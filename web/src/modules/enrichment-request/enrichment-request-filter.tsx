import {
	Box,
	Button,
	FormControl,
	Grid,
	InputLabel,
	MenuItem,
	Paper,
	Select,
	SelectChangeEvent,
	TextField,
	Typography,
} from '@mui/material'
import { EnrichmentRequestFilterDto } from 'pages/enrichment/enrichment-request-list'
import { ChangeEvent, FC, useState } from 'react'
import {
	DigitalObjectModel,
	DigitalObjectModelMapping,
	publicationModelList,
} from '../../enums/publication-model'
import { RequestState, RequestStateMapping } from '../../models/request/request'

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

	const handleStateChange = (event: SelectChangeEvent) => {
		const val = event.target.value

		setFilter(prevFilter => ({
			...prevFilter,
			state: val as RequestState,
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
					<Grid item lg={3} xs={12}>
						<FormControl fullWidth size="small">
							<InputLabel id="demo-simple-select-label">Stav</InputLabel>
							<Select
								label="Stav"
								value={filter.state}
								onChange={handleStateChange}
							>
								<MenuItem value={undefined}>Všechny</MenuItem>
								{Object.entries(RequestStateMapping).map(([key, value]) => (
									<MenuItem key={key} value={key}>
										{value}
									</MenuItem>
								))}
							</Select>
						</FormControl>
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
