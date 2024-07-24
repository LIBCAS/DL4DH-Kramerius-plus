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
import {
	OrderType,
	RootFilterOperation,
	SortField,
	UserRequestListFilter,
} from 'api/user-request-api'
import { RequestState, requestStates, RequestType } from 'models/user-requests'
import { ChangeEvent, useState } from 'react'

export const UserRequestFilters = ({
	doFilter,
}: {
	doFilter: (filter: UserRequestListFilter) => void
}) => {
	const [filters, setFilters] = useState<UserRequestListFilter>({})

	const onSubmit = () => {
		doFilter(filters)
	}

	const onYearChange = (event: ChangeEvent<HTMLInputElement>) => {
		setFilters({ ...filters, year: parseInt(event.target.value) })
	}

	const onIdentificationChange = (event: ChangeEvent<HTMLInputElement>) => {
		setFilters({ ...filters, identification: parseInt(event.target.value) })
	}

	const onStateChange = (event: SelectChangeEvent) => {
		setFilters({ ...filters, state: event.target.value as RequestState })
	}

	const onTypeChange = (event: SelectChangeEvent) => {
		setFilters({ ...filters, type: event.target.value as RequestType })
	}

	const onAuthorChange = (event: ChangeEvent<HTMLInputElement>) => {
		setFilters({ ...filters, username: event.target.value })
	}

	const onRootFilterOperationChange = (event: SelectChangeEvent) => {
		setFilters({
			...filters,
			rootFilterOperation: event.target.value as RootFilterOperation,
		})
	}

	const onOrderChange = (event: SelectChangeEvent) => {
		setFilters({ ...filters, sortOrder: event.target.value as OrderType })
	}

	const onFieldChange = (event: SelectChangeEvent) => {
		setFilters({ ...filters, sortField: event.target.value as SortField })
	}

	return (
		<Paper elevation={2}>
			<Box component="form" sx={{ p: 2 }}>
				<Box sx={{ pb: 3 }}>
					<Typography variant="h5">Filtrování</Typography>
				</Box>
				<Grid container spacing={3}>
					<Grid item xs={2}>
						<TextField
							fullWidth
							id="year"
							label="Rok"
							size="small"
							type="number"
							onChange={onYearChange}
						/>
					</Grid>
					<Grid item xs={2}>
						<TextField
							fullWidth
							id="identification"
							label="Poradové číslo"
							size="small"
							type="number"
							onChange={onIdentificationChange}
						/>
					</Grid>
					<Grid item xs={2}>
						<FormControl fullWidth size="small">
							<InputLabel id="state">Stav</InputLabel>
							<Select
								fullWidth
								id="state"
								label="Stav"
								onChange={onStateChange}
							>
								{requestStates.map((state, index) => (
									<MenuItem key={index} value={state}>
										{state}
									</MenuItem>
								))}
							</Select>
						</FormControl>
					</Grid>
					<Grid item xs={2}>
						<FormControl fullWidth size="small">
							<InputLabel id="type">Typ</InputLabel>
							<Select fullWidth id="type" label="Typ" onChange={onTypeChange}>
								<MenuItem value={'EXPORT'}>Export</MenuItem>
								<MenuItem value={'ENRICHMENT'}>Obohacení</MenuItem>
							</Select>
						</FormControl>
					</Grid>
					<Grid item xs={2}>
						<TextField
							fullWidth
							id="author"
							label="Autor"
							size="small"
							onChange={onAuthorChange}
						/>
					</Grid>
					<Grid item xs={2}>
						<FormControl fullWidth size="small">
							<InputLabel id="rootFilterOperation">
								Operace mezi filtry
							</InputLabel>
							<Select
								defaultValue={'AND'}
								fullWidth
								id="rootFilterOperation"
								label="Operace mezi filtry"
								onChange={onRootFilterOperationChange}
							>
								<MenuItem value={'AND'}>Všechny</MenuItem>
								<MenuItem value={'OR'}>Aspoň jeden</MenuItem>
							</Select>
						</FormControl>
					</Grid>
					<Grid item xs={1}>
						<Box alignItems="center" display="flex">
							<Typography>Uspořádaní</Typography>
						</Box>
					</Grid>
					<Grid item xs={2}>
						<FormControl fullWidth size="small">
							<InputLabel id="order">Pořadí</InputLabel>
							<Select
								defaultValue={'DESC'}
								fullWidth
								id="order"
								label="Pořadí"
								size="small"
								onChange={onOrderChange}
							>
								<MenuItem value={'DESC'}>Najvyšší první</MenuItem>
								<MenuItem value={'ASC'}>Najnižší první</MenuItem>
							</Select>
						</FormControl>
					</Grid>
					<Grid item xs={2}>
						<FormControl fullWidth size="small">
							<InputLabel id="field">Pole</InputLabel>
							<Select
								defaultValue={'CREATED'}
								fullWidth
								id="type"
								label="Třídit podle"
								onChange={onFieldChange}
							>
								<MenuItem value={'CREATED'}>Vytvořeno</MenuItem>
								<MenuItem value={'UPDATED'}>Změněno</MenuItem>
								<MenuItem value={'AUTHOR'}>Autor</MenuItem>
							</Select>
						</FormControl>
					</Grid>
				</Grid>
				<Box sx={{ paddingTop: 3 }}>
					<Button color="primary" variant="contained" onClick={onSubmit}>
						Filtrovat
					</Button>
				</Box>
			</Box>
		</Paper>
	)
}
