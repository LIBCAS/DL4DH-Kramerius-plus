import {
	Box,
	Button,
	Divider,
	Grid,
	LinearProgress,
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
import { ChangeEvent, FC, useState } from 'react'
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

const initialEmptyState = {
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

enum StorageKeys {
	CUSTOM_CONFIGS = 'custom-enrichment-configs',
	ENRICHMENT_PROFILE = 'enrichment-profile',
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

const getProfileConfig = (profile: EnrichmentProfile) => {
	if (profile === EnrichmentProfile.CUSTOM) {
		return (
			JSON.parse(localStorage.getItem(StorageKeys.CUSTOM_CONFIGS) ?? 'null') ??
			[]
		)
	}
	return profiles.find(p => p.type === profile)?.configs
}

export const EnrichmentPage: FC = () => {
	const location = useLocation()
	const statesFromLocation = (location?.state?.requests ?? []) as StateType[]
	const [states] = useState<StateType[]>(
		statesFromLocation.map(s => ({
			...initialEmptyState,
			name: s.name,
			publicationIds: (s.publicationIds as unknown as string[]).join(', '),
		})),
	)

	const savedSelectedProfileType = (JSON.parse(
		localStorage.getItem(StorageKeys.ENRICHMENT_PROFILE) ?? 'null',
	) ?? EnrichmentProfile.BASIC) as EnrichmentProfile

	return (
		<>
			{states.length === 0 && (
				<EnrichmentItem
					initState={{
						...initialEmptyState,
						configs: getProfileConfig(savedSelectedProfileType),
					}}
					initialProfile={savedSelectedProfileType}
				/>
			)}
			{states.length > 1 && (
				<>
					<PageWrapper requireAuth>
						<Grid container justifyContent="center">
							<Grid display="flex" item justifyContent="end" mt={3} xs={7}>
								<Button color="primary" variant="contained">
									Odeslat vše
								</Button>
							</Grid>
						</Grid>
					</PageWrapper>
				</>
			)}
			{states.map((state, index) => (
				<EnrichmentItem
					key={`state-${index}`}
					initState={{
						...state,
						configs: getProfileConfig(savedSelectedProfileType),
					}}
					initialProfile={savedSelectedProfileType}
				/>
			))}
		</>
	)
}

export const EnrichmentItem: FC<{
	initState: StateType
	initialProfile: EnrichmentProfile
}> = ({ initState, initialProfile }) => {
	const [state, setState] = useState<StateType>({
		...initState,
	})

	const [localCustomConfig, setLocalCustomConfig] = useState<
		EnrichmentJobConfig[]
	>(getProfileConfig(EnrichmentProfile.CUSTOM))

	const setConfigs = (configs: EnrichmentJobConfig[]) => {
		setState(prev => ({ ...prev, configs: configs }))
	}

	const [selectedProfile, setProfile] =
		useState<EnrichmentProfile>(initialProfile)

	const [done, setDone] = useState(false)
	const [loading, setLoading] = useState(false)

	const onSubmit = () => {
		const sendRequest = async (publicationIds: string[]) => {
			setLoading(true)
			const response = await enrich(publicationIds, state.configs, state.name)
			setLoading(false)
			if (response.ok) {
				toast('Operace proběhla úspěšně', {
					type: 'success',
				})
				setDone(true)
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
			toast('Žádost musí obsahovat minimálně jednu publikaci!', {
				type: 'error',
			})
		} else {
			sendRequest(state.publicationIds.split(/[\s,]+/))
		}
	}

	const onUuidsChange = (event: ChangeEvent<HTMLInputElement>) => {
		setState(prev => ({ ...prev, publicationIds: event.target.value }))
	}

	const onNameChange = (event: ChangeEvent<HTMLInputElement>) => {
		setState(prev => ({ ...prev, name: event.target.value }))
	}

	return (
		<PageWrapper requireAuth>
			<Grid container justifyContent="center">
				<Grid item xs={7}>
					<Paper elevation={4} sx={{ minHeight: 400, p: 2 }}>
						<PageBlock title="Nová žádost o obohacení">
							<Grid container spacing={2}>
								<Grid item xs={12}>
									<TextField
										disabled={loading || done}
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
										disabled={loading || done}
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
									<Tabs value={selectedProfile}>
										{profiles.map((profile, index) => (
											<Tab
												key={index}
												disabled={loading || done}
												label={profile.label}
												style={{ width: '25%' }}
												value={profile.type}
												onClick={() => {
													localStorage.setItem(
														StorageKeys.ENRICHMENT_PROFILE,
														JSON.stringify(profile.type),
													)
													setProfile(profile.type)

													if (profile.type === EnrichmentProfile.CUSTOM) {
														setConfigs(localCustomConfig)
													} else {
														setConfigs(getProfileConfig(profile.type))
													}
												}}
											/>
										))}
									</Tabs>

									<EnrichmentConfigs
										configs={state.configs || []}
										disabled={selectedProfile !== EnrichmentProfile.CUSTOM}
										setConfigs={configs => {
											setConfigs(configs)
											setLocalCustomConfig(configs)
											localStorage.setItem(
												StorageKeys.CUSTOM_CONFIGS,
												JSON.stringify(configs),
											)
										}}
									/>
								</Grid>
								<Grid item xs={12}>
									{loading ? (
										<LinearProgress
											color="warning"
											style={{ width: '100%', height: 2 }}
										/>
									) : (
										<Divider style={{ height: 2 }} />
									)}
								</Grid>

								<Grid display="flex" item justifyContent="right" xs={12}>
									{done ? (
										<>
											<Box
												bgcolor="#2e7d32"
												borderRadius={2}
												color="white"
												fontSize="sm"
												p={1}
											>
												ODESLÁNO
											</Box>
										</>
									) : (
										<Button
											disabled={loading}
											variant="contained"
											onClick={onSubmit}
										>
											Odeslat
										</Button>
									)}
								</Grid>
							</Grid>
						</PageBlock>
					</Paper>
				</Grid>
			</Grid>
		</PageWrapper>
	)
}
