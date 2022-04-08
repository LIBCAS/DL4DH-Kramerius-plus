import { GridValueFormatterParams } from '@mui/x-data-grid'

export const dateTimeFormatter = (params: GridValueFormatterParams) => {
	if (params.value === undefined) {
		return undefined
	}
	const date = new Date(params.value as string)
	return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}:${date.getMilliseconds()}`
}

export const durationFormatter = (params: GridValueFormatterParams) => {
	const durationInMillis = params.value as number
	if (durationInMillis === undefined) {
		return undefined
	}

	return new Date(durationInMillis).toISOString().slice(11, -1)
}
