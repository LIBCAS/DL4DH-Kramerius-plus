import Grid from '@material-ui/core/Grid'
import TextField from '@material-ui/core/TextField'
import { makeStyles } from '@material-ui/core/styles'
import Button from '@material-ui/core/Button'
import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline'
import IconButton from '@material-ui/core/IconButton'
import CloseIcon from '@material-ui/icons/Close'
import Typography from '@material-ui/core/Typography'
import { v4 } from 'uuid'

import { TeiParams, Params } from '../../models'
import { Filter } from '../../models/filter'

type Props = {
	filters: Filter[]
	setParams: React.Dispatch<React.SetStateAction<Params | TeiParams>>
}

const useStyles = makeStyles(() => ({
	title: {
		marginBottom: 8,
	},
	input: {
		'& input': {
			padding: '8px 10px',
		},
		'& > div': {
			paddingRight: 8,
		},
		marginBottom: 8,
	},
	button: {
		padding: '2px 6px',
		textTransform: 'none',
	},
}))

export const ExportFilters = ({ filters, setParams }: Props) => {
	const classes = useStyles()

	const addFilter = () =>
		setParams(p => ({
			...p,
			filters: [
				...filters,
				{ field: '', value: '', operation: 'EQ', id: v4() },
			],
		}))

	const removeFilter = (id: string) => {
		const newFilters = filters.filter(f => f.id !== id)

		setParams(p => ({
			...p,
			filters: newFilters,
		}))
	}

	const handleFieldChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const newFilters = filters.map(f => {
			if (f.id === e.target.name) {
				return {
					...f,
					field: e.target.value,
				}
			}

			return f
		})

		setParams(p => ({
			...p,
			filters: newFilters,
		}))
	}

	const handleValueChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const newFilters = filters.map(f => {
			if (f.id === e.target.name) {
				return {
					...f,
					value: e.target.value,
				}
			}

			return f
		})
		setParams(p => ({
			...p,
			filters: newFilters,
		}))
	}

	return (
		<div>
			<Typography className={classes.title}>Filtry</Typography>
			{filters.map(({ id, field, value }) => (
				<Grid key={id} container spacing={1}>
					<Grid item xs={11}>
						<Grid container spacing={2}>
							<Grid item xs={6}>
								<TextField
									classes={{ root: classes.input }}
									fullWidth
									name={id}
									placeholder="Vložte název"
									value={field}
									variant="outlined"
									onChange={handleFieldChange}
								/>
							</Grid>
							<Grid item xs={6}>
								<TextField
									classes={{ root: classes.input }}
									fullWidth
									name={id}
									placeholder="Vložte hodnotu"
									value={value}
									variant="outlined"
									onChange={handleValueChange}
								/>
							</Grid>
						</Grid>
					</Grid>
					<Grid item xs={1}>
						<IconButton
							color="primary"
							size="small"
							onClick={() => removeFilter(id)}
						>
							<CloseIcon />
						</IconButton>
					</Grid>
				</Grid>
			))}

			<Button
				className={classes.button}
				color="primary"
				startIcon={<AddCircleOutlineIcon />}
				onClick={addFilter}
			>
				Přidat filtr
			</Button>
		</div>
	)
}
