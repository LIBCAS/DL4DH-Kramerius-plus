import { GridValueFormatterParams } from '@mui/x-data-grid'
import { KrameriusUser } from 'models/domain/kramerius-user'

export const dateTimeFormatter = (params: GridValueFormatterParams) => {
	if (params.value === undefined) {
		return undefined
	}
	const date = new Date(params.value as string)
	return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}.${date.getMilliseconds()}`
}

export function formatDateTime(dateString: string) {
	const date = new Date(dateString)
	return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`
}

export const durationFormatter = (params: GridValueFormatterParams) => {
	const durationInMillis = params.value as number
	if (durationInMillis === undefined) {
		return undefined
	}

	return new Date(durationInMillis).toISOString().slice(11, -1)
}

export const ownerFormatter = (params: GridValueFormatterParams) => {
	if (params.value == undefined) {
		return '-'
	}

	const owner = params.value as KrameriusUser
	return owner.username
}
