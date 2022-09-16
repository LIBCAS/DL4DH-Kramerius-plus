import { FormEvent } from 'react'
import { toast } from 'react-toastify'
import { customFetch } from 'utils/custom-fetch'

export const download = (fileRefId: string) => async (event: FormEvent) => {
	let filename = ''
	downloadExport(fileRefId)
		.then(response => {
			if (response.ok) {
				filename =
					response.headers
						.get('content-disposition')
						?.split('; ')[1]
						.split('=')[1] ?? 'file'

				filename = filename?.substring(1, filename.length - 1)

				return response.blob()
			} else {
				throw Error(response.statusText)
			}
		})
		.then(blob => {
			const url = window.URL.createObjectURL(new Blob([blob]))
			const link = document.createElement('a')
			link.href = url
			link.setAttribute('download', filename)

			document.body.appendChild(link)
			link.click()
			link.parentNode?.removeChild(link)
		})
		.catch(e => {
			toast(`Nastala chyba při stahování: ${e}`, {
				type: 'error',
			})
		})

	event.preventDefault()
}

export const downloadExport = async (id: string): Promise<Response> => {
	return await customFetch(`/api/files/${id}`)
}
