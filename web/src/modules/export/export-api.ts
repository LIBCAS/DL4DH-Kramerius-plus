import { FileRef } from 'models'
import fetch from 'utils/fetch'

export const getExportedPublications = async () => {
	try {
		const response = await fetch('/api/export/list', {
			method: 'GET',
			headers: new Headers({ 'Content-Type': 'application/json' }),
		})

		const json: FileRef[] = await response.json()

		return json
	} catch (e) {
		return []
	}
}
