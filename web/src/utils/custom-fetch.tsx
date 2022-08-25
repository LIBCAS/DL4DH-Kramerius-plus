export const customFetch = async (
	url: string,
	request?: RequestInit,
): Promise<Response> => {
	const token = localStorage.getItem('token')

	let headers = request?.headers ? { ...request?.headers } : new Headers()

	headers = { ...headers, 'Content-type': 'application/json' }

	if (token) {
		console.log('Adding token to headers')
		headers = { ...headers, Authorization: `Bearer ${token}` }
	}

	return await fetch(url, {
		...request,
		headers: headers,
	})
}
