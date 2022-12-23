import { GridValueFormatterParams } from '@mui/x-data-grid'
import moment from 'moment'

export const dateTimeFormatter = (params: GridValueFormatterParams) => {
	if (params.value === undefined) {
		return undefined
	}
	const date = new Date(params.value as string)
	return moment(date).format('DD.MM.YYYY HH:mm:ss.SSS')
}

export function formatDateTime(dateString: string) {
	const date = new Date(dateString)
	// return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`
	return moment(date).format('DD.MM.YYYY HH:mm:ss.SSS')
}

export const durationFormatter = (params: GridValueFormatterParams) => {
	const durationInMillis = params.value as number
	if (durationInMillis === undefined) {
		return undefined
	}

	return new Date(durationInMillis).toISOString().slice(11, -1)
}
