import {
	Button,
	Divider,
	Grid,
	Paper,
	Tab,
	Tabs,
	TextField,
	Typography,
} from '@mui/material'
import { enrich } from 'api/enrichment-api'
import { PageBlock } from 'components/page-block'
import { EnrichmentJobConfig } from 'models/job/config/enrichment-job-config'
import { EnrichmentConfigs } from 'modules/enrichment-form/enrichment-configs'
import { ChangeEvent, FC, useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import { PageWrapper } from '../page-wrapper'
import { EnrichmentKrameriusJob } from 'enums/enrichment-kramerius-job'
import { v4 as uuid } from 'uuid'

import { useLocation } from 'react-router-dom'

type StateType = {
	publicationIds: string
	configs: EnrichmentJobConfig[]
	name?: string
}

const initialState = {
	publicationIds: '',
	configs: [],
	name: '',
}

export enum EnrichmentProfile {
	BASIC = 'BASIC',
	UPDATE = 'UPDATE',
	COMPLETE = 'COMPLETE',
	CUSTOM = 'CUSTOM',
}

type EnrichmentProfileType = {
	type: EnrichmentProfile
	configs: EnrichmentJobConfig[]
	label: string
}

const profiles: EnrichmentProfileType[] = [
	{
		type: EnrichmentProfile.BASIC,
		configs: [
			{
				id: uuid(),
				override: false,
				pageErrorTolerance: 0,
				jobType: EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL,
			},
			{
				id: uuid(),
				override: false,
				pageErrorTolerance: 0,
				jobType: EnrichmentKrameriusJob.ENRICHMENT_TEI,
			},
		],
		label: 'Základ',
	},
	{
		type: EnrichmentProfile.UPDATE,
		configs: [
			{
				id: uuid(),
				override: true,
				pageErrorTolerance: 0,
				jobType: EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL,
			},
			{
				id: uuid(),
				override: true,
				pageErrorTolerance: 0,
				jobType: EnrichmentKrameriusJob.ENRICHMENT_TEI,
			},
		],
		label: 'Aktualizace',
	},
	{
		type: EnrichmentProfile.COMPLETE,
		configs: [
			{
				id: uuid(),
				override: true,
				pageErrorTolerance: 0,
				jobType: EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL,
			},
			{
				id: uuid(),
				override: true,
				pageErrorTolerance: 0,
				jobType: EnrichmentKrameriusJob.ENRICHMENT_TEI,
			},
			{
				id: uuid(),
				override: true,
				pageErrorTolerance: 0,
				jobType: EnrichmentKrameriusJob.ENRICHMENT_NDK,
			},
		],
		label: 'Komplet',
	},
	{
		type: EnrichmentProfile.CUSTOM,
		configs: [],
		label: 'Vlastní',
	},
]

export const EnrichmentPage: FC = () => {
	const location = useLocation()
	const [state, setState] = useState<StateType>({
		...initialState,
		name: (location?.state?.requests ?? [])?.[0]?.name,
		publicationIds: (location?.state?.requests ??
			[])?.[0]?.publicationIds?.join?.(', '),
	})

	const setConfigs = (configs: EnrichmentJobConfig[]) => {
		setState(prev => ({ ...prev, configs: configs }))
	}

	const onSubmit = () => {
		const sendRequest = async (publicationIds: string[]) => {
			const response = await enrich(publicationIds, state.configs, state.name)
			if (response.ok) {
				toast('Operace proběhla úspěšně', {
					type: 'success',
				})
			} else {
				toast(
					`Při pokusu o vytvoření plánu nastala chyba: ${response.statusText}`,
					{
						type: 'error',
					},
				)
			}
		}

		if (!state.configs.length) {
			toast('Konfigurace musí obsahovat alespoň jednu úlohu!', {
				type: 'error',
			})
		} else if (!state.publicationIds || !state.publicationIds.length) {
			toast('Žádost musí obsahovať aspoň jednu publikaci!', { type: 'error' })
		} else {
			sendRequest(state.publicationIds.split(/[\s,]+/))
			setState({ ...initialState })
		}
	}

	const onUuidsChange = (event: ChangeEvent<HTMLInputElement>) => {
		setState(prev => ({ ...prev, publicationIds: event.target.value }))
	}

	const onNameChange = (event: ChangeEvent<HTMLInputElement>) => {
		setState(prev => ({ ...prev, name: event.target.value }))
	}
	const [selectedProfile, setProfile] = useState<EnrichmentProfileType>(
		profiles[0],
	)

	useEffect(() => {
		if (selectedProfile.type !== EnrichmentProfile.CUSTOM) {
			setConfigs(selectedProfile.configs)
		}
	}, [selectedProfile])

	return (
		<PageWrapper requireAuth>
			<Grid container justifyContent="center">
				<Grid item xs={7}>
					<Paper elevation={4} sx={{ minHeight: 400, p: 2 }}>
						<PageBlock title="Nová žádost o obohacení">
							<Grid container spacing={2}>
								<Grid item xs={12}>
									<TextField
										fullWidth
										label="Název žádosti"
										size="small"
										value={state.name}
										onChange={onNameChange}
									/>
								</Grid>
								<Grid item xs={12}>
									<Divider />
								</Grid>
								<Grid item xs={12}>
									<TextField
										fullWidth
										label="UUID publikací"
										multiline
										placeholder="Zadejte UUID publikací, které chcete obohatit, oddělené čárkou nebo mezerou"
										required
										rows={4}
										size="small"
										value={state.publicationIds}
										onChange={onUuidsChange}
									/>
								</Grid>
								<Grid item xs={12}>
									<Divider />
								</Grid>
								<Grid item xs={12}>
									<Typography variant="h6">Profil obohacení</Typography>
									<Tabs
										value={selectedProfile.type}
										onChange={props => console.log({ props })}
									>
										{profiles.map((profile, index) => (
											<Tab
												key={index}
												label={profile.label}
												style={{ width: '25%' }}
												value={profile.type}
												onClick={() => setProfile(profile)}
											/>
										))}
									</Tabs>
									{selectedProfile.type === EnrichmentProfile.CUSTOM && (
										<EnrichmentConfigs
											configs={state.configs || []}
											setConfigs={setConfigs}
										/>
									)}
								</Grid>
								<Grid item xs={12}>
									<Divider />
								</Grid>
								<Grid display="flex" item justifyContent="right" xs={12}>
									<Button variant="contained" onClick={onSubmit}>
										Odeslat
									</Button>
								</Grid>
							</Grid>
						</PageBlock>
					</Paper>
				</Grid>
			</Grid>
		</PageWrapper>
	)
}
