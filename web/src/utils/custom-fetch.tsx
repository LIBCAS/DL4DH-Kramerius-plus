import { KEYCLOAK_TOKEN } from 'keycloak'

export const customFetch = async (
	url: string,
	request?: RequestInit,
): Promise<Response> => {
	const token = localStorage.getItem(KEYCLOAK_TOKEN)

	let headers = request?.headers ? { ...request?.headers } : new Headers()

	headers = { ...headers, 'Content-type': 'application/json' }

	if (token) {
		headers = { ...headers, Authorization: `Bearer ${token}` }
	}

	const response = await fetch(url, {
		...request,
		headers: headers,
	})

	if (response.status === 403) {
		window.location.href = '/'
	}

	return response
}

export const customFetchWithContentType = async (
	url: string,
	request?: RequestInit,
): Promise<Response> => {
	const token = localStorage.getItem(KEYCLOAK_TOKEN)

	let headers = request?.headers ? { ...request?.headers } : new Headers()

	headers = { ...headers }

	if (token) {
		headers = { ...headers, Authorization: `Bearer ${token}` }
	}

	const response = await fetch(url, {
		...request,
		headers: headers,
	})

	if (response.status === 403) {
		window.location.href = '/'
	}

	return response
}
