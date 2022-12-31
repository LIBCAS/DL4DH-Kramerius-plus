import { Box } from '@mui/material'
import React, { FormEvent } from 'react'
import { EnrichmentJobConfig } from '../../models/job/config/enrichment-job-config'

export type CurrentConfig = {
	index?: number
	config: EnrichmentJobConfig
}

const initialPlan = {
	publicationIds: [''],
	configs: [],
	name: '',
}

export const EnrichmentForm = () => {
	// const [plan, setPlan] = useState<JobPlanCreate>(initialPlan)
	// const [showDialog, setShowDialog] = useState<boolean>(false)
	// const [currentConfig, setCurrentConfig] = useState<CurrentConfig>()

	// const removeUuidField = (index: number) => {
	// 	setPlan(plan => ({
	// 		...plan,
	// 		publicationIds: plan.publicationIds.filter((_field, i) => i != index),
	// 	}))
	// }

	// const changeUuidField = (index: number, value: string) => {
	// 	setPlan(plan => ({
	// 		...plan,
	// 		publicationIds: plan.publicationIds.map((uuid, i) =>
	// 			index === i ? value : uuid,
	// 		),
	// 	}))
	// }

	// const addUuidField = () => {
	// 	setPlan(plan => ({
	// 		...plan,
	// 		publicationIds: [...plan.publicationIds, ''],
	// 	}))
	// }

	// const onConfigClick = (index: number) => {
	// 	setCurrentConfig({ config: { ...plan.configs[index] }, index })
	// 	setShowDialog(true)
	// }

	// const onConfigClose = () => {
	// 	setShowDialog(false)
	// 	setCurrentConfig(undefined)
	// }

	// const onConfigRemove = (index: number) => {
	// 	setPlan(plan => ({
	// 		...plan,
	// 		configs: plan.configs.filter((_, i) => i !== index),
	// 	}))
	// }

	// const onOverrideChange = () => {
	// 	setCurrentConfig(currentConfig => ({
	// 		...currentConfig,
	// 		config: {
	// 			...currentConfig!.config,
	// 			override: !currentConfig!.config.override,
	// 		},
	// 	}))
	// }

	// const onPageErrorToleranceChange = (newValue: number) => {
	// 	setCurrentConfig(currentConfig => ({
	// 		...currentConfig,
	// 		config: {
	// 			...currentConfig!.config,
	// 			pageErrorTolerance: newValue,
	// 		} as ExternalEnrichmentJobEventConfig,
	// 	}))
	// }

	// const onPublicationErrorToleranceChange = (newValue: number) => {
	// 	setCurrentConfig(currentConfig => ({
	// 		...currentConfig,
	// 		config: {
	// 			...currentConfig!.config,
	// 			publicationErrorTolerance: newValue,
	// 		} as ExternalEnrichmentJobEventConfig,
	// 	}))
	// }

	// const onDialogSubmit = () => {
	// 	setShowDialog(false)
	// 	if (currentConfig!.index !== undefined) {
	// 		setPlan(plan => ({
	// 			...plan,
	// 			configs: plan.configs.map((config, i) => {
	// 				if (i === currentConfig!.index) {
	// 					return { ...currentConfig!.config }
	// 				} else {
	// 					return config
	// 				}
	// 			}),
	// 		}))
	// 	} else {
	// 		setPlan(plan => ({
	// 			...plan,
	// 			configs: [...plan.configs, { ...currentConfig!.config }],
	// 		}))
	// 	}

	// 	setCurrentConfig(undefined)
	// }

	// const onNewConfigClick = (krameriusJob: EnrichmentKrameriusJob) => {
	// 	if (krameriusJob === 'ENRICHMENT_EXTERNAL') {
	// 		setCurrentConfig({
	// 			config: {
	// 				krameriusJob,
	// 				override: false,
	// 				// pageErrorTolerance: 0
	// 			} as ExternalEnrichmentJobEventConfig,
	// 		})
	// 	} else if (krameriusJob === 'ENRICHMENT_NDK') {
	// 		setCurrentConfig({
	// 			config: {
	// 				krameriusJob,
	// 				override: false,
	// 			} as NdkEnrichmentJobEventConfig,
	// 		})
	// 	} else if (krameriusJob === 'ENRICHMENT_TEI') {
	// 		setCurrentConfig({
	// 			config: {
	// 				krameriusJob,
	// 				override: false,
	// 			} as TeiEnrichmentJobEventConfig,
	// 		})
	// 	} else {
	// 		setCurrentConfig({
	// 			config: {
	// 				krameriusJob,
	// 				override: false,
	// 			},
	// 		})
	// 	}
	// 	setShowDialog(true)
	// }

	// const onNameChange = (value: string) => {
	// 	setPlan(plan => ({ ...plan, name: value }))
	// }

	const onFormSubmit = (event: FormEvent) => {
		// async function sendRequest() {
		// 	const response = await enrich(
		// 		plan.publicationIds,
		// 		plan.configs,
		// 		plan.name,
		// 	)
		// 	if (response.ok) {
		// 		toast('Operace proběhla úspěšně', {
		// 			type: 'success',
		// 		})
		// 		setPlan(initialPlan)
		// 	} else {
		// 		toast(
		// 			`Při pokusu o vytvoření plánu nastala chyba: ${response.statusText}`,
		// 			{
		// 				type: 'error',
		// 			},
		// 		)
		// 	}
		// }
		// event.preventDefault()
		// if (!plan.configs.length) {
		// 	toast('Konfigurace musí obsahovat alespoň jednu úlohu!', {
		// 		type: 'error',
		// 	})
		// 	return
		// } else {
		// 	sendRequest()
		// }
	}

	// const isNew = () => {
	// 	return currentConfig?.index === undefined
	// }

	// const jobTypeItem = (jobType: KrameriusJob) => (
	// 	<Box
	// 		display="flex"
	// 		justifyContent="space-between"
	// 		sx={{ height: 50, pl: 2, pr: 2 }}
	// 	>
	// 		<Box>
	// 			<Typography variant="body1">Typ úlohy</Typography>
	// 		</Box>
	// 		<Box>
	// 			<Typography variant="body1">{KrameriusJobMapping[jobType]}</Typography>
	// 		</Box>
	// 	</Box>
	// )

	// const overrideExistingItem = (checked: boolean) => (
	// 	<Box
	// 		display="flex"
	// 		justifyContent="space-between"
	// 		sx={{ height: 50, pl: 2, pr: 2 }}
	// 	>
	// 		<Box>
	// 			<Typography variant="body1">Přepsat existující</Typography>
	// 		</Box>
	// 		<Box>
	// 			<Checkbox checked={checked} onChange={onOverrideChange} />
	// 		</Box>
	// 	</Box>
	// )

	// const publicationErrorToleranceItem = (defaultValue: number) => {
	// 	return (
	// 		<Box
	// 			display="flex"
	// 			justifyContent="space-between"
	// 			sx={{ height: 50, pl: 2, pr: 2 }}
	// 		>
	// 			<Box>
	// 				<Typography variant="body1">Tolerance chyb v publikacích</Typography>
	// 			</Box>
	// 			<Box sx={{ width: 100 }}>
	// 				<TextField
	// 					defaultValue={defaultValue || 0}
	// 					size="small"
	// 					type="number"
	// 					onChange={e =>
	// 						onPublicationErrorToleranceChange(parseInt(e.target.value))
	// 					}
	// 				/>
	// 			</Box>
	// 		</Box>
	// 	)
	// }

	// const pageErrorToleranceItem = (defaultValue: number) => (
	// 	<Box
	// 		display="flex"
	// 		justifyContent="space-between"
	// 		sx={{ height: 50, pl: 2, pr: 2 }}
	// 	>
	// 		<Box>
	// 			<Typography variant="body1">Tolerance chyb v stránkach</Typography>
	// 		</Box>
	// 		<Box sx={{ width: 100 }}>
	// 			<TextField
	// 				defaultValue={defaultValue || 0}
	// 				size="small"
	// 				type="number"
	// 				onChange={e => onPageErrorToleranceChange(parseInt(e.target.value))}
	// 			/>
	// 		</Box>
	// 	</Box>
	// )

	// const enrichmentKrameriusConfig = (jobConfig: EnrichmentJobEventConfig) => (
	// 	<Stack>
	// 		{jobTypeItem(EnrichmentKrameriusJob.ENRICHMENT_KRAMERIUS)}
	// 		{overrideExistingItem(jobConfig.override)}
	// 	</Stack>
	// )

	// const externalKrameriusConfig = (
	// 	jobConfig: ExternalEnrichmentJobEventConfig,
	// ) => (
	// 	<Stack>
	// 		{jobTypeItem(EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL)}
	// 		{overrideExistingItem(jobConfig.override)}
	// 		{publicationErrorToleranceItem(jobConfig.publicationErrorTolerance)}
	// 		{pageErrorToleranceItem(jobConfig.pageErrorTolerance)}
	// 	</Stack>
	// )

	// const ndkKrameriusConfig = (jobConfig: NdkEnrichmentJobEventConfig) => (
	// 	<Stack>
	// 		{jobTypeItem(EnrichmentKrameriusJob.ENRICHMENT_NDK)}
	// 		{overrideExistingItem(jobConfig.override)}
	// 		{publicationErrorToleranceItem(jobConfig.publicationErrorTolerance)}
	// 		{pageErrorToleranceItem(jobConfig.pageErrorTolerance)}
	// 	</Stack>
	// )

	// const teiKrameriusConfig = (jobConfig: TeiEnrichmentJobEventConfig) => (
	// 	<Stack>
	// 		{jobTypeItem(EnrichmentKrameriusJob.ENRICHMENT_TEI)}
	// 		{overrideExistingItem(jobConfig.override)}
	// 		{publicationErrorToleranceItem(jobConfig.publicationErrorTolerance)}
	// 		{pageErrorToleranceItem(jobConfig.pageErrorTolerance)}
	// 	</Stack>
	// )

	// const getCurrentConfig = () => {
	// 	if (currentConfig) {
	// 		if (currentConfig.config.krameriusJob === 'ENRICHMENT_KRAMERIUS') {
	// 			return enrichmentKrameriusConfig(currentConfig.config)
	// 		}
	// 		if (currentConfig.config.krameriusJob === 'ENRICHMENT_EXTERNAL') {
	// 			return externalKrameriusConfig(
	// 				currentConfig.config as ExternalEnrichmentJobEventConfig,
	// 			)
	// 		}
	// 		if (currentConfig.config.krameriusJob === 'ENRICHMENT_NDK') {
	// 			return ndkKrameriusConfig(
	// 				currentConfig.config as NdkEnrichmentJobEventConfig,
	// 			)
	// 		}
	// 		if (currentConfig.config.krameriusJob === 'ENRICHMENT_TEI') {
	// 			return teiKrameriusConfig(
	// 				currentConfig.config as TeiEnrichmentJobEventConfig,
	// 			)
	// 		}
	// 	}
	// }

	return (
		<Box component="form" sx={{ p: 3 }} onSubmit={onFormSubmit}>
			{/* <Grid container spacing={2}>
				<Grid item xs={12}>
					<Typography sx={{ marginBottom: 2 }} variant="h5">
						Vytvoření nového plánu obohacení
					</Typography>
				</Grid>
				<Grid item xs={12}>
					<EnrichmentAccordion
						configProps={{
							configs: plan.configs,
							onConfigClick,
							onConfigRemove,
							onNewConfigClick,
						}}
						nameProps={{
							fieldValue: plan.name,
							onFieldChange: onNameChange,
						}}
						uuidProps={{
							removeUuidField,
							changeUuidField,
							addUuidField,
							fields: plan.publicationIds,
						}}
					/>
				</Grid>
				<Grid item xs={12}>
					<Button type="submit" variant="contained">
						Vytvořit
					</Button>
				</Grid>
			</Grid>
			{currentConfig && (
				<Dialog fullWidth open={showDialog} onClose={onConfigClose}>
					<DialogTitle>
						{isNew() ? 'Přidat novou' : 'Upravit'} konfiguraci
					</DialogTitle>
					<DialogContent>{getCurrentConfig()}</DialogContent>
					<DialogActions disableSpacing={true}>
						<Button
							sx={{ marginBottom: 1, marginRight: 2 }}
							variant="contained"
							onClick={onDialogSubmit}
						>
							{isNew() ? 'Přidat' : 'Upravit'}
						</Button>
					</DialogActions>
				</Dialog>
			)} */}
		</Box>
	)
}
