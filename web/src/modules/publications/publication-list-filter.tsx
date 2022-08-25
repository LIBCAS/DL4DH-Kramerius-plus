import {
	Paper,
	Grid,
	Typography,
	TextField,
	Button,
	ToggleButton,
	ToggleButtonGroup,
	Box,
} from '@mui/material'
import { ChangeEvent, FC } from 'react'
import { CustomDateTimePicker } from './custom-date-time-picker'
import { PublicationFilter } from '../../api/publication-api'

type Props = {
	onTextChange: (
		key: keyof PublicationFilter,
	) => (event: ChangeEvent<HTMLInputElement>) => void
	onDateChange: (key: keyof PublicationFilter) => (value: Date | null) => void
	onPublishedChange: (
		event: React.MouseEvent<HTMLElement>,
		isPublished: boolean,
	) => void
	onFilterClick: () => void
	filter: PublicationFilter
}

export const PublicationListFilter: FC<Props> = ({
	onTextChange,
	onDateChange,
	onFilterClick,
	onPublishedChange,
	filter,
}) => {
	return (
		<Paper variant="outlined">
			<Box component="form" sx={{ p: 2 }}>
				<Grid container spacing={3}>
					<Grid item xs={12}>
						<Typography variant="h5">Filtrování</Typography>
					</Grid>
					<Grid item lg={6} xs={12}>
						<TextField
							fullWidth
							label="Název"
							size="small"
							value={filter.title}
							onChange={onTextChange('title')}
						/>
					</Grid>
					<Grid item lg={6} xs={12}>
						<TextField
							fullWidth
							label="UUID rodiče"
							size="small"
							value={filter.parentId}
							onChange={onTextChange('parentId')}
						/>
					</Grid>
					<Grid item lg={6} xs={12}>
						<CustomDateTimePicker
							label="Vytvořeno před"
							value={filter.createdBefore || null}
							onChange={onDateChange('createdBefore')}
						/>
					</Grid>
					<Grid item lg={6} xs={12}>
						<CustomDateTimePicker
							label="Vytvořeno po"
							value={filter.createdAfter || null}
							onChange={onDateChange('createdAfter')}
						/>
					</Grid>
					<Grid item lg={12} md={12} xl={12} xs={12}>
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
					<Grid item lg={6} xs={12}>
						<CustomDateTimePicker
							disabled={!filter.isPublished}
							label="Publikováno před"
							value={filter.publishedBefore || null}
							onChange={onDateChange('publishedBefore')}
						/>
					</Grid>
					<Grid item lg={6} xs={12}>
						<CustomDateTimePicker
							disabled={!filter.isPublished}
							label="Publikováno po"
							value={filter.publishedAfter || null}
							onChange={onDateChange('publishedAfter')}
						/>
					</Grid>
				</Grid>
				<Box sx={{ paddingTop: 4 }}>
					<Button variant="contained" onClick={onFilterClick}>
						Filtrovat
					</Button>
				</Box>
			</Box>
		</Paper>
	)
}
