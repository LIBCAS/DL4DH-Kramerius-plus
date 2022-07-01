import { useState, useMemo, useContext } from 'react'
import { v4 } from 'uuid'
import Grid from '@material-ui/core/Grid'
import TextField from '@material-ui/core/TextField'
import Button from '@material-ui/core/Button'
import IconButton from '@material-ui/core/IconButton'
import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline'
import Paper from '@material-ui/core/Paper'
import InputAdornment from '@material-ui/core/InputAdornment'
import Typography from '@material-ui/core/Typography'
import RemoveCircleOutlineIcon from '@material-ui/icons/RemoveCircleOutline'
import { makeStyles } from '@material-ui/core/styles'
import { toast } from 'react-toastify'

import { ApiError } from 'models'

import { DialogContext } from 'components/dialog/dialog-context'

import { createPlan, downloadKStructure } from './enrichment-api'
import { DefaultDialog } from 'components/dialog/knav-dialog/knav-default-dialog'
import { Box } from '@mui/system'
import { Stack } from '@mui/material'
import {
	Avatar,
	List,
	ListItem,
	ListItemSecondaryAction,
	ListItemText,
} from '@material-ui/core'
import { EnrichmentDialog } from './enrichment-dialog'
import DeleteIcon from '@material-ui/icons/Delete'
import { EnrichmentJobEventConfigType } from 'models/job/config/dto/enrichment/enrichment-job-event-config-type'
import { MissingAltoStrategy } from '../../enums/missing-alto-strategy'
import { EnrichmentKrameriusJob } from 'enums/enrichment-kramerius-job'

const useStyles = makeStyles(() => ({
	addButton: {
		padding: '2px 6px',
		textTransform: 'none',
	},
	paper: {
		padding: '10px 24px',
		minHeight: 140,
	},
	configPaper: {
		marginTop: '10px',
		padding: '10px 24px',
		minHeight: 20,
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
	formTitle: {
		marginBottom: 10,
	},
	submitButton: {
		display: 'flex',
		justifyContent: 'flex-end',
		alignItems: 'flex-start',
	},
	list: {
		width: '100%',
		maxWidth: 360,
	},
	avatar: {
		margin: '10px',
	},
}))

type Fields = {
	id: string
	value: string
}

const initialValue = { id: v4(), value: '' }

const initialJobConfig = {
	krameriusJob: EnrichmentKrameriusJob.ENRICHMENT_KRAMERIUS,
	override: false,
}

export const EnrichmentForm = () => {
	const classes = useStyles()
	const [showDialog, setShowDialog] = useState<boolean>(false)
	const [currentConfig, setCurrentConfig] =
		useState<EnrichmentJobEventConfigType>(initialJobConfig)
	const [idFields, setIdFields] = useState<Fields[]>([initialValue])
	const [jobEventConfigs, setJobEventConfigs] = useState<
		EnrichmentJobEventConfigType[]
	>([initialJobConfig])
	const [configIndex, setConfigIndex] = useState<number>()
	const [planName, setPlanName] = useState<string>()
	const { open } = useContext(DialogContext)

	const disabledSubmitButton = useMemo(() => {
		const foundEmptyValueIndex = idFields.findIndex(f => !f.value)

		return foundEmptyValueIndex !== -1
	}, [idFields])

	// adds field
	const addField = () =>
		setIdFields(idFields => [...idFields, { id: v4(), value: '' }])

	const addConfigField = (
		config: EnrichmentJobEventConfigType,
		configIndex?: number,
	) => {
		if (configIndex !== undefined) {
			const editedConfigs = jobEventConfigs.map((obj, i) => {
				if (i === configIndex) {
					return config
				} else {
					return obj
				}
			})
			setJobEventConfigs(editedConfigs)
		} else {
			setJobEventConfigs(jobEventConfigs => [...jobEventConfigs, config])
		}
	}

	const removeConfigField = (index: number) => {
		const newFields = [...jobEventConfigs]
		newFields.splice(index, 1)
		setJobEventConfigs(newFields)
	}

	// removes field
	const removeField = (id: string) => {
		const newFields = idFields.filter(f => f.id !== id)
		setIdFields(newFields)
	}

	const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
		e.preventDefault()

		const publications = idFields.map(f => f.value.trim())

		const response = await createPlan(publications, jobEventConfigs, planName)

		if (response.ok) {
			toast('Operace proběhla úspěšně', {
				type: 'success',
			})

			setIdFields([initialValue])
		} else {
			const { code } = response.data as ApiError

			if (code === 'ALREADY_ENRICHED') {
				open({
					Content: () => (
						<DefaultDialog
							contentHeight={25}
							title="Opakované obohacení"
							onSubmit={async () => {
								const response = await downloadKStructure(publications, true)

								if (response.ok) {
									toast('Opakované obohacení proběhlo úspěšně', {
										type: 'success',
									})

									setIdFields([initialValue])
								}
							}}
						>
							Jedna nebo více publikací již existuje. Přejete si je obohatit
							znovu?
						</DefaultDialog>
					),
					size: 'md',
				})
			} else {
				toast('Při pokusu o obohacení nastala chyba.', {
					type: 'error',
				})
			}
		}
	}

	const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const newFields = idFields.map(f => {
			if (f.id === e.target.name) {
				return {
					...f,
					value: e.target.value,
				}
			}

			return f
		})

		setIdFields(newFields)
	}

	const handlePlanNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		setPlanName(e.target.value)
	}

	const onConfigClick = (krameriusJob: EnrichmentKrameriusJob) => {
		if (krameriusJob === 'ENRICHMENT_EXTERNAL') {
			setCurrentConfig({
				krameriusJob,
				override: false,
				missingAltoOption: MissingAltoStrategy.FAIL_IF_ALL_MISS,
			})
		} else {
			setCurrentConfig({ krameriusJob, override: false })
		}
		setShowDialog(true)
	}

	const onExistingConfigClick = (
		config: EnrichmentJobEventConfigType,
		index: number,
	) => {
		setConfigIndex(index)
		setCurrentConfig(config)
		setShowDialog(true)
	}

	const onDialogSubmit = (
		config: EnrichmentJobEventConfigType,
		configIndex?: number,
	) => {
		addConfigField(config, configIndex)
		onClose()
	}

	const onClose = () => {
		setShowDialog(false)
		setConfigIndex(undefined)
	}

	return (
		<form onSubmit={handleSubmit}>
			<Grid container justifyContent="space-between" spacing={2}>
				<Grid item xs={5}>
					<Paper className={classes.paper}>
						<Box sx={{ pb: 2 }}>
							<Typography color="primary" variant="h6">
								UUID publikací:
							</Typography>
						</Box>
						<Box>
							{idFields.map(({ id, value }, i) => (
								<TextField
									key={id}
									InputProps={{
										endAdornment:
											i !== 0 ? (
												<InputAdornment position="end">
													<IconButton
														color="primary"
														size="small"
														onClick={() => removeField(id)}
													>
														<RemoveCircleOutlineIcon fontSize="small" />
													</IconButton>
												</InputAdornment>
											) : (
												<></>
											),
									}}
									classes={{ root: classes.input }}
									fullWidth
									name={id}
									placeholder="Vložte uuid publikace"
									value={value}
									variant="outlined"
									onChange={handleChange}
								/>
							))}
							<Button
								className={classes.addButton}
								color="primary"
								startIcon={<AddCircleOutlineIcon />}
								onClick={addField}
							>
								Přidat publikaci
							</Button>
						</Box>
					</Paper>
				</Grid>
				<Grid item xs={4}>
					<Paper className={classes.paper}>
						<Box sx={{ pb: 2 }}>
							<Typography color="primary" variant="h5">
								Plán obohacení
							</Typography>
						</Box>
						<Box sx={{ pb: 2 }}>
							<Box sx={{ pb: 1 }}>
								<Typography color="primary" variant="h6">
									Název:
								</Typography>
							</Box>
							<Box>
								<TextField
									classes={{ root: classes.input }}
									fullWidth
									placeholder="Vložte název plánu"
									value={planName}
									variant="outlined"
									onChange={handlePlanNameChange}
								/>
							</Box>
						</Box>
						<Box sx={{ pb: 1 }}>
							<Typography color="primary" variant="h6">
								Konfigurace:
							</Typography>
						</Box>
						<List>
							{jobEventConfigs.map((config, i) => (
								<ListItem
									key={i}
									button
									onClick={() => onExistingConfigClick(config, i)}
								>
									<Avatar className={classes.avatar}>{i + 1}.</Avatar>
									<ListItemText
										primary={config.krameriusJob}
										secondary={'Přepsat: ' + config.override}
									/>
									<ListItemSecondaryAction onClick={() => removeConfigField(i)}>
										<IconButton aria-label="delete" edge="end">
											<DeleteIcon />
										</IconButton>
									</ListItemSecondaryAction>
								</ListItem>
							))}
						</List>
						<Box sx={{ p: 2 }}>
							<Box sx={{ pb: 2 }}>
								<Typography color="primary" variant="body1">
									Přidat konfiguraci
								</Typography>
							</Box>
							<Stack spacing={1}>
								<Button
									className={classes.addButton}
									color="primary"
									startIcon={<AddCircleOutlineIcon />}
									variant="contained"
									onClick={() =>
										onConfigClick(EnrichmentKrameriusJob.ENRICHMENT_KRAMERIUS)
									}
								>
									STAHOVÁNÍ DAT
								</Button>
								<Button
									className={classes.addButton}
									color="primary"
									startIcon={<AddCircleOutlineIcon />}
									variant="contained"
									onClick={() =>
										onConfigClick(EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL)
									}
								>
									OBOHACENÍ EXTERNÍMI NÁSTROJI
								</Button>
								<Button
									className={classes.addButton}
									color="primary"
									startIcon={<AddCircleOutlineIcon />}
									variant="contained"
									onClick={() =>
										onConfigClick(EnrichmentKrameriusJob.ENRICHMENT_NDK)
									}
								>
									OBOHACENÍ NDK
								</Button>
								<Button
									className={classes.addButton}
									color="primary"
									startIcon={<AddCircleOutlineIcon />}
									variant="contained"
									onClick={() =>
										onConfigClick(EnrichmentKrameriusJob.ENRICHMENT_TEI)
									}
								>
									OBOHACENÍ TEI
								</Button>
							</Stack>
						</Box>
					</Paper>
				</Grid>
				<Grid className={classes.submitButton} item xs={2}>
					<Button
						color="primary"
						disabled={disabledSubmitButton}
						type="submit"
						variant="contained"
					>
						Vytvořit plán obohacení
					</Button>
				</Grid>
			</Grid>
			<EnrichmentDialog
				configIndex={configIndex}
				existingConfig={currentConfig}
				showDialog={showDialog}
				text={configIndex !== undefined ? 'Upravit' : 'Přidat'}
				onClose={onClose}
				onSubmit={onDialogSubmit}
			/>
		</form>
	)
}
