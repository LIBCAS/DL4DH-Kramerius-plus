import { get, isEmpty } from 'lodash'
import { ApiError } from 'models'

export const defaultOptions = {
	credentials: 'same-origin' as const,
}

/**
 * Error populated with message.
 */
export interface ErrorMessage {
	status: number
	code: string
  message: string
}

interface FetchType {
	method?: string
	params?: any

	body?: any
	headers?: Headers

	errorHandler?: (error: ApiError) => ErrorMessage
}

/**
 * If no message formatter is provided, returned plain stringified error code.
 */
export function getErrorMessage(error: ApiError): ErrorMessage {
	return {
    status: error.status,
		code: error.code,
		message: error.message,
	}
}

/**
 * Encode query params.
 *
 * @param obj
 */
export const serialize = (obj: any = {}) =>
	Object.keys(obj)
		.map(key => `${encodeURIComponent(key)}=${encodeURIComponent(obj[key])}`)
		.join('&')

/**
 * Error handler.
 *
 * @param response
 * @param errorHandler
 */
async function handleErrors(
	response: Promise<Response>,
	errorHandler: (error: ApiError) => ErrorMessage,
): Promise<Response> {
	const r = await response

	if (r.redirected) {
		window.location.reload()
	}

	// something went wrong
	if (!r.ok) {
		// Not authorized
		// if (r.status === 401) {
		// 	window.location.href = `${window.location.origin}/logout`
		// }
		const err = await r.json()

		// custom error
		throw errorHandler(err)
	}

	return r
}

/**
 * Fetch utility.
 */
export default function (url: string, { params, ...rest }: FetchType = {}) {
	const options = { ...defaultOptions, ...rest }
	const headers = get(options, 'headers') || new Headers()

	const queryString = serialize(params)
	const questionMark = isEmpty(queryString) ? '' : '?'

	return handleErrors(
		fetch(`${url}${questionMark}${queryString}`, {
			...options,
			headers,
		}),
		options.errorHandler ?? getErrorMessage,
	)
}
