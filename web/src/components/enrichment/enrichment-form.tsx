import { Grid, Typography, Button, Box } from '@mui/material'
import { EnrichmentAccordion } from './enrichment-accordion'
import { FormEvent, useState } from 'react'
import { JobPlanCreate } from '../../models/job/job-plan-create'
import { EnrichmentKrameriusJob } from '../../enums/enrichment-kramerius-job'
import { ExternalEnrichmentJobEventConfig } from '../../models/job/config/enrichment/external-enrichment-job-event-config'
import { MissingAltoStrategy } from '../../enums/missing-alto-strategy'
import { EnrichmentJobEventConfig } from '../../models/job/config/enrichment/enrichment-job-event-config'
import { ConfigDialog } from './config-accordion/config-dialog'
import { toast } from 'react-toastify'
import { createPlan } from '../../api/enrichment-api'

const initialCurrentConfig: EnrichmentJobEventConfig = {
	override: true,
	krameriusJob: EnrichmentKrameriusJob.ENRICHMENT_KRAMERIUS,
}

export type CurrentConfig = {
	index?: number
	config: EnrichmentJobEventConfig
}

const initialPlan = {
	publicationIds: [''],
	configs: [],
	name: '',
}

export const EnrichmentForm = () => {
	const [plan, setPlan] = useState<JobPlanCreate>(initialPlan)
	const [showDialog, setShowDialog] = useState<boolean>(false)
	const [currentConfig, setCurrentConfig] = useState<CurrentConfig>({
		config: { ...initialCurrentConfig },
	})

	const removeUuidField = (index: number) => {
		setPlan(plan => ({
			...plan,
			publicationIds: plan.publicationIds.filter((_field, i) => i != index),
		}))
	}

	const changeUuidField = (index: number, value: string) => {
		setPlan(plan => ({
			...plan,
			publicationIds: plan.publicationIds.map((uuid, i) =>
				index === i ? value : uuid,
			),
		}))
	}

	const addUuidField = () => {
		setPlan(plan => ({
			...plan,
			publicationIds: [...plan.publicationIds, ''],
		}))
	}

	const onConfigClick = (index: number) => {
		setCurrentConfig({ config: { ...plan.configs[index] }, index })
		setShowDialog(true)
	}

	const onConfigClose = () => {
		setShowDialog(false)
	}

	const onConfigRemove = (index: number) => {
		setPlan(plan => ({
			...plan,
			configs: plan.configs.filter((_, i) => i !== index),
		}))
	}

	const onOverrideChange = () => {
		setCurrentConfig(currentConfig => ({
			...currentConfig,
			config: {
				...currentConfig.config,
				override: !currentConfig.config.override,
			},
		}))
	}

	const onStrategyChange = (newStrategy: MissingAltoStrategy) => {
		setCurrentConfig(currentConfig => ({
			...currentConfig,
			config: {
				...currentConfig.config,
				missingAltoOption: newStrategy,
			} as ExternalEnrichmentJobEventConfig,
		}))
	}

	const onDialogSubmit = () => {
		setShowDialog(false)
		if (currentConfig.index !== undefined) {
			setPlan(plan => ({
				...plan,
				configs: plan.configs.map((config, i) => {
					if (i === currentConfig.index) {
						return { ...currentConfig.config }
					} else {
						return config
					}
				}),
			}))
		} else {
			setPlan(plan => ({
				...plan,
				configs: [...plan.configs, { ...currentConfig.config }],
			}))
		}
	}

	const onNewConfigClick = (krameriusJob: EnrichmentKrameriusJob) => {
		if (krameriusJob === 'ENRICHMENT_EXTERNAL') {
			setCurrentConfig({
				config: {
					krameriusJob: krameriusJob,
					override: false,
					missingAltoOption: MissingAltoStrategy.FAIL_IF_ALL_MISS,
				} as ExternalEnrichmentJobEventConfig,
			})
		} else {
			setCurrentConfig({
				config: {
					krameriusJob: krameriusJob,
					override: false,
				},
			})
		}
		setShowDialog(true)
	}

	const onNameChange = (value: string) => {
		setPlan(plan => ({ ...plan, name: value }))
	}

	const onFormSubmit = (event: FormEvent) => {
		async function sendRequest() {
			const response = await createPlan(
				plan.publicationIds,
				plan.configs,
				plan.name,
			)

			if (response.ok) {
				toast('Operace proběhla úspěšně', {
					type: 'success',
				})

				setPlan(initialPlan)
			} else {
				toast(
					`Při pokusu o vytvoření plánu nastala chyba: ${response.statusText}`,
					{
						type: 'error',
					},
				)
			}
		}
		event.preventDefault()

		if (!plan.configs.length) {
			toast('Konfigurace musí obsahovat alespoň jednu úlohu!', {
				type: 'error',
			})
			return
		} else {
			sendRequest()
		}
	}

	return (
		<Box component="form" sx={{ p: 3 }} onSubmit={onFormSubmit}>
			<Grid container spacing={2}>
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
			<ConfigDialog
				currentConfig={currentConfig}
				open={showDialog}
				onClose={onConfigClose}
				onOverrideChange={onOverrideChange}
				onStrategyChange={onStrategyChange}
				onSubmit={onDialogSubmit}
			/>
		</Box>
	)
}
