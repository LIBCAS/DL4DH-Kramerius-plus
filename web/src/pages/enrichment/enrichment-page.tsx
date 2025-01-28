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
import { ChangeEvent, FC, useCallback, useMemo, useState } from 'react'
import { toast } from 'react-toastify'
import { PageWrapper } from '../page-wrapper'

import { useLocation } from 'react-router-dom'
import {
	EnrichmentProfile,
	enrichmentProfiles,
	EnrichmentStorageKeys,
} from './enrichment-profiles'

import { v4 as uuid } from 'uuid'
import _ from 'lodash'

export type StateType = {
	publicationIds: string
	configs: EnrichmentJobConfig[]
	name?: string
}

const getProfileConfig = (profile: EnrichmentProfile) => {
	if (profile === EnrichmentProfile.CUSTOM) {
		return (
			JSON.parse(
				localStorage.getItem(EnrichmentStorageKeys.CUSTOM_CONFIGS) ?? 'null',
			) ?? []
		)
	}
	return enrichmentProfiles.find(p => p.type === profile)?.configs
}

const initialEmptyState = {
	publicationIds: '',
	configs: [],
	name: '',
}

type EnrichmentItem = {
	id: string
	state: StateType
	loading: boolean
	done: boolean
	error: string
	initialProfile: EnrichmentProfile
}

const useInitItems = (profile: EnrichmentProfile): EnrichmentItem[] => {
	const location = useLocation()

	return useMemo(() => {
		const statesFromLocation = (location?.state?.requests ?? []) as StateType[]
		if (statesFromLocation.length === 0) {
			return [
				{
					id: uuid(),
					done: false,
					loading: false,
					error: '',
					initialProfile: profile,
					state: {
						...initialEmptyState,
						configs: getProfileConfig(profile),
					},
				},
			]
		}
		return statesFromLocation.map(s => ({
			state: {
				...initialEmptyState,
				name: s.name,
				publicationIds: (s.publicationIds as unknown as string[]).join(', '),
				configs: getProfileConfig(profile),
			},
			id: uuid(),
			initialProfile: profile,
			loading: false,
			done: false,
			error: '',
		}))
	}, [location?.state?.requests, profile])
}

export const EnrichmentPage: FC = () => {
	const savedSelectedProfileType = (JSON.parse(
		localStorage.getItem(EnrichmentStorageKeys.ENRICHMENT_PROFILE) ?? 'null',
	) ?? EnrichmentProfile.BASIC) as EnrichmentProfile

	const [items, setItems] = useState<EnrichmentItem[]>(
		useInitItems(savedSelectedProfileType),
	)
	const [bulkLoading, setBulkLoading] = useState(false)
	const [bulkError, setBulkError] = useState(false)

	const updateState = useCallback((id: string, state: StateType) => {
		setItems(items => {
			const index = items.findIndex(i => i.id === id)
			if (index === -1) {
				return items
			}
			return [
				...items.slice(0, index),
				{ ...items[index], state: { ...items[index].state, ...state } },
				...items.slice(index + 1),
			]
		})
	}, [])

	const submitSingleRequest = async (
		item: EnrichmentItem,
		setLoading: (loading: boolean) => void,
	) => {
		const sendRequest = async (publicationIds: string[]) => {
			setLoading(true)
			try {
				//const response = await later(1000)
				const response = await enrich(
					publicationIds,
					item.state.configs,
					item.state.name,
				)
				setLoading(false)
				if (response.ok) {
					toast('Operace proběhla úspěšně', {
						type: 'success',
					})
					setItems(items => {
						const index = items.findIndex(i => i.id === item.id)
						if (index === -1) {
							return items
						}
						return [
							...items.slice(0, index),
							{ ...items[index], done: true, error: '' },
							...items.slice(index + 1),
						]
					})
				} else {
					toast(
						`Při pokusu o vytvoření plánu nastala chyba: ${response.statusText}`,
						{
							type: 'error',
						},
					)
				}
			} catch (error) {
				setLoading(false)
				toast(`Při pokusu o vytvoření plánu nastala chyba: ${error}`, {
					type: 'error',
				})
			}
		}

		if (!item.state.configs.length) {
			toast('Konfigurace musí obsahovat alespoň jednu úlohu!', {
				type: 'error',
			})
		} else if (
			!item.state.publicationIds ||
			!item.state.publicationIds.length
		) {
			toast('Žádost musí obsahovat minimálně jednu publikaci!', {
				type: 'error',
			})
		} else {
			sendRequest(item.state.publicationIds.split(/[\s,]+/))
		}
	}

	const submitAll = useCallback(async () => {
		setBulkLoading(true)
		let isError = false
		setBulkError(isError)
		const validatedItems = items.map(item => {
			if (item.done) {
				return item
			}
			if (item.state.configs.length === 0) {
				isError = true
				return {
					...item,
					error: 'Konfigurace musí obsahovat alespoň jednu úlohu!',
				}
			} else if (
				!item.state.publicationIds ||
				!item.state.publicationIds.length
			) {
				isError = true
				return {
					...item,
					error: 'Žádost musí obsahovat minimálně jednu publikaci!',
				}
			} else {
				return { ...item, error: '' }
			}
		})

		const promises = validatedItems
			.filter(item => !item.done && !item.error)
			.map(item => {
				return {
					id: item.id,
					//promise: later(Math.random() * 500),
					promise: enrich(
						item.state.publicationIds.split(/[\s,]+/),
						item.state.configs,
						item.state.name,
					),
				}
			})

		const responses = await Promise.allSettled(promises.map(p => p.promise))

		isError = isError ? isError : responses.some(r => r.status === 'rejected')

		setItems(() => {
			return validatedItems.map(item => {
				if (item.done || item.error) return item
				const promiseIndex = promises.findIndex(p => p.id === item.id)
				return {
					...item,
					done: responses[promiseIndex].status === 'fulfilled',
					error:
						responses[promiseIndex].status === 'rejected'
							? (_.get(responses[promiseIndex], 'reason') as string) ||
							  'Odeslání selhalo'
							: '',
				}
			})
		})
		setBulkError(isError)
		setBulkLoading(false)
	}, [items])

	return (
		<>
			{items.length > 1 && (
				<>
					<PageWrapper requireAuth>
						<Grid container justifyContent="center">
							<Grid
								display="flex"
								item
								justifyContent="space-between"
								mt={3}
								xs={7}
							>
								{bulkError ? (
									<Box color="red" fontWeight="bold">
										Upozornění: některé žádosti nebyly odeslány.
									</Box>
								) : (
									<div />
								)}
								<Button
									color="primary"
									disabled={bulkLoading}
									variant="contained"
									onClick={submitAll}
								>
									Odeslat vše
								</Button>
							</Grid>
							{bulkLoading && <LinearProgress />}
						</Grid>
					</PageWrapper>
				</>
			)}

			{items.map((item, index) => (
				<EnrichmentItem
					key={`state-${index}`}
					bulkLoading={bulkLoading}
					item={item}
					submitSingleRequest={submitSingleRequest}
					updateState={updateState}
				/>
			))}
		</>
	)
}

export const EnrichmentItem: FC<{
	item: EnrichmentItem
	updateState: (id: string, state: StateType) => void
	bulkLoading: boolean
	submitSingleRequest: (
		item: EnrichmentItem,
		setLoading: (loading: boolean) => void,
	) => Promise<void>
}> = ({ item, submitSingleRequest, updateState, bulkLoading }) => {
	const [localCustomConfig, setLocalCustomConfig] = useState<
		EnrichmentJobConfig[]
	>(getProfileConfig(EnrichmentProfile.CUSTOM))

	const setConfigs = (configs: EnrichmentJobConfig[]) => {
		updateState(item.id, { ...item.state, configs })
	}

	const [selectedProfile, setProfile] = useState<EnrichmentProfile>(
		item.initialProfile,
	)

	const [localLoading, setLoading] = useState(false)

	const onUuidsChange = (event: ChangeEvent<HTMLInputElement>) => {
		updateState(item.id, { ...item.state, publicationIds: event.target.value })
	}

	const onNameChange = (event: ChangeEvent<HTMLInputElement>) => {
		updateState(item.id, { ...item.state, name: event.target.value })
	}

	const done = item.done
	const error = item.error
	const loading = item.loading || bulkLoading || localLoading

	return (
		<PageWrapper requireAuth>
			<Grid container justifyContent="center">
				<Grid item xs={7}>
					<Paper
						elevation={4}
						sx={{
							minHeight: 400,
							p: 2,
							border: `2px solid ${error ? 'red' : done ? 'green' : 'white'}`,
						}}
					>
						<PageBlock title="Nová žádost o obohacení">
							<Grid container spacing={2}>
								<Grid item xs={12}>
									<TextField
										disabled={loading || done}
										fullWidth
										label="Název žádosti"
										size="small"
										value={item.state.name}
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
										value={item.state.publicationIds}
										onChange={onUuidsChange}
									/>
								</Grid>
								<Grid item xs={12}>
									<Divider />
								</Grid>
								<Grid item xs={12}>
									<Typography variant="h6">Profil obohacení</Typography>
									<Tabs value={selectedProfile}>
										{enrichmentProfiles.map((profile, index) => (
											<Tab
												key={index}
												disabled={loading || done}
												label={profile.label}
												style={{ width: '25%' }}
												value={profile.type}
												onClick={() => {
													localStorage.setItem(
														EnrichmentStorageKeys.ENRICHMENT_PROFILE,
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
										configs={item.state.configs || []}
										disabled={
											selectedProfile !== EnrichmentProfile.CUSTOM || done
										}
										setConfigs={configs => {
											setConfigs(configs)
											setLocalCustomConfig(configs)
											localStorage.setItem(
												EnrichmentStorageKeys.CUSTOM_CONFIGS,
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

								<Grid
									display="flex"
									item
									justifyContent="space-between"
									xs={12}
								>
									{error ? (
										<Box color="red" fontWeight="bold">
											{error}
										</Box>
									) : (
										<Box />
									)}
									{done ? (
										<>
											<Box
												bgcolor="#2e7d32"
												borderRadius={1}
												color="white"
												fontSize="sm"
												p={1}
											>
												<Typography fontSize={14}>ODESLÁNO</Typography>
											</Box>
										</>
									) : (
										<Button
											disabled={loading}
											variant="contained"
											onClick={() => submitSingleRequest(item, setLoading)}
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
