import {
	Paper,
	Grid,
	Typography,
	TextField,
	Button,
	ToggleButton,
	ToggleButtonGroup,
	Box,
	Select,
	SelectChangeEvent,
	MenuItem,
	FormControl,
	InputLabel,
} from '@mui/material'
import { ChangeEvent, FC, useState } from 'react'
import { CustomDateTimePicker } from './custom-date-time-picker'
import { PublicationFilter } from '../../api/publication-api'
import {
	DigitalObjectModel,
	DigitalObjectModelMapping,
	publicationModelList,
} from 'enums/publication-model'

type Props = {
	onSubmit: (filter: PublicationFilter) => void
}

export const PublicationListFilter: FC<Props> = ({ onSubmit }) => {
	const [filter, setFilter] = useState<PublicationFilter>({
		parentId: '',
		model: '',
	} as PublicationFilter)

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

	const onPublishedChange = (
		event: React.MouseEvent<HTMLElement>,
		isPublished: boolean,
	) => {
		setFilter(prevFilter => ({
			...prevFilter,
			isPublished,
		}))
	}

	const handleModelChange = (event: SelectChangeEvent) => {
		const val = event.target.value

		setFilter(prevFilter => ({
			...prevFilter,
			model: val as DigitalObjectModel,
		}))
	}

	const onDateChange =
		(key: keyof PublicationFilter) => (value: Date | null) => {
			setFilter(prevFilter => ({ ...prevFilter, [key]: value }))
		}

	return (
		<Paper elevation={2}>
			<Box component="form" sx={{ p: 2 }}>
				<Box sx={{ pb: 3 }}>
					<Typography variant="h5">Filtrování</Typography>
				</Box>
				<Grid container spacing={3}>
					<Grid item xs={3}>
						<TextField
							fullWidth
							label="UUID"
							size="small"
							value={filter.uuid}
							onChange={handleFieldChange('uuid')}
						/>
					</Grid>
					<Grid item xs={3}>
						<TextField
							fullWidth
							label="Název"
							size="small"
							value={filter.title}
							onChange={handleFieldChange('title')}
						/>
					</Grid>
					<Grid item xs={2}>
						<FormControl fullWidth size="small">
							<InputLabel id="demo-simple-select-label">Model</InputLabel>
							<Select
								label="Model"
								value={filter.model}
								onChange={handleModelChange}
							>
								<MenuItem value={''}>Všechny</MenuItem>
								{publicationModelList.map(model => (
									<MenuItem key={model} value={model}>
										{DigitalObjectModelMapping[model]}
									</MenuItem>
								))}
							</Select>
						</FormControl>
					</Grid>
					<Grid item lg={2} xs={12}>
						<CustomDateTimePicker
							label="Vytvořeno před"
							value={filter.createdBefore || null}
							onChange={onDateChange('createdBefore')}
						/>
					</Grid>
					<Grid item lg={2} xs={12}>
						<CustomDateTimePicker
							label="Vytvořeno po"
							value={filter.createdAfter || null}
							onChange={onDateChange('createdAfter')}
						/>
					</Grid>
					<Grid item lg={2}>
						<Typography variant="h6">Zobrazit jenom:</Typography>
					</Grid>
					<Grid item lg={6} md={12}>
						<ToggleButtonGroup
							color="primary"
							exclusive
							fullWidth
							size="small"
							value={filter.isPublished}
							onChange={onPublishedChange}
						>
							<ToggleButton value={true}>Publikované</ToggleButton>
							<ToggleButton value={false}>Nepublikované</ToggleButton>
						</ToggleButtonGroup>
					</Grid>
					<Grid item lg={2} xs={12}>
						<CustomDateTimePicker
							disabled={!filter.isPublished}
							label="Publikováno před"
							value={filter.publishedBefore || null}
							onChange={onDateChange('publishedBefore')}
						/>
					</Grid>
					<Grid item lg={2} xs={12}>
						<CustomDateTimePicker
							disabled={!filter.isPublished}
							label="Publikováno po"
							value={filter.publishedAfter || null}
							onChange={onDateChange('publishedAfter')}
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
