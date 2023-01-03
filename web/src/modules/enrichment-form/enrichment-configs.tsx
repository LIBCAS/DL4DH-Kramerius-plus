import {
	Button,
	Checkbox,
	Dialog,
	DialogActions,
	DialogContent,
	DialogTitle,
	Grid,
	TextField,
	Typography,
} from '@mui/material'
import { ConfigButtons } from 'components/enrichment/config-buttons'
import { ConfigList } from 'components/enrichment/config-list'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { EnrichmentKrameriusJob } from 'enums/enrichment-kramerius-job'
import { EnrichmentJobConfig } from 'models/job/config/enrichment-job-config'
import { FC, useState } from 'react'
import { v4 } from 'uuid'

export type CurrentConfig = {
	index?: number
	config: EnrichmentJobConfig
}

export const EnrichmentConfigs: FC<{
	configs: EnrichmentJobConfig[]
	setConfigs: (configs: EnrichmentJobConfig[]) => void
}> = ({ configs, setConfigs }) => {
	const [currentConfig, setCurrentConfig] = useState<EnrichmentJobConfig>()

	const onNewConfigClick = (jobType: EnrichmentKrameriusJob) => {
		setCurrentConfig({
			id: v4(),
			jobType: jobType,
			override: false,
			pageErrorTolerance: 0,
		})
	}

	const isNew = () => {
		return !configs.find(config => config.id === currentConfig?.id)
	}

	const onExistingConfigClick = (id: string) => {
		setCurrentConfig(configs.find(config => config.id === id))
	}

	const onExistingConfigRemove = (id: string) => {
		setConfigs(configs.filter(config => config.id !== id))
	}

	const onDialogClose = () => {
		setCurrentConfig(undefined)
	}

	const onSubmit = () => {
		if (isNew()) {
			setConfigs([...configs, currentConfig!])
		} else {
			setConfigs(
				configs.map(config => {
					if (config.id === currentConfig?.id) {
						return currentConfig
					}

					return config
				}),
			)
		}
		setCurrentConfig(undefined)
	}

	const onOverrideChange = (event: React.ChangeEvent<HTMLInputElement>) => {
		setCurrentConfig(prev => ({
			...prev!,
			override: event.target.checked,
		}))
	}

	const onPageToleranceChange = (
		event: React.ChangeEvent<HTMLInputElement>,
	) => {
		setCurrentConfig(prev => ({
			...prev!,
			pageErrorTolerance: parseInt(event.target.value),
		}))
	}

	return (
		<Grid container spacing={2}>
			<Grid item xs={12}>
				<ConfigList
					configs={configs}
					onClick={onExistingConfigClick}
					onRemove={onExistingConfigRemove}
				/>
			</Grid>
			<Grid item xs={12}>
				<ConfigButtons onClick={onNewConfigClick} />
			</Grid>

			<Dialog
				fullWidth
				open={!!currentConfig}
				sx={{ p: 20 }}
				onClose={onDialogClose}
			>
				<DialogTitle>
					{isNew() ? 'Přidat novou' : 'Upravit'} konfiguraci
				</DialogTitle>
				<DialogContent>
					<Grid container justifyContent="space-between" spacing={2}>
						<Grid item xs={5}>
							<Typography variant="body1">Typ úlohy</Typography>
						</Grid>
						<Grid display="flex" item justifyContent="right" xs={7}>
							<Typography variant="body1">
								{
									KrameriusJobMapping[
										currentConfig?.jobType ? currentConfig.jobType : ''
									]
								}
							</Typography>
						</Grid>
						<Grid item xs={5}>
							<Typography variant="body1">Přepsat existující</Typography>
						</Grid>
						<Grid display="flex" item justifyContent="right" xs={7}>
							<Checkbox
								checked={!!currentConfig?.override}
								size="small"
								sx={{ height: 20 }}
								onChange={onOverrideChange}
							/>
						</Grid>
						<Grid item xs={5}>
							<Typography variant="body1">
								Tolerance chyb v stránkach
							</Typography>
						</Grid>
						<Grid display="flex" item justifyContent="right" xs={7}>
							<TextField
								size="small"
								sx={{ height: 20, width: 100 }}
								type="number"
								value={currentConfig?.pageErrorTolerance}
								onChange={onPageToleranceChange}
							/>
						</Grid>
					</Grid>
				</DialogContent>
				<DialogActions disableSpacing={true}>
					<Button
						sx={{ marginBottom: 1, marginRight: 2 }}
						variant="contained"
						onClick={onSubmit}
					>
						{isNew() ? 'Přidat' : 'Upravit'}
					</Button>
				</DialogActions>
			</Dialog>
		</Grid>
	)
}
