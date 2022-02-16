import { InstanceInfo } from 'models'
import { useEffect, useState } from 'react'

import fetch from 'utils/fetch'

export const useNavbar = () => {
	const [data, setData] = useState<InstanceInfo>()

	useEffect(() => {
		const getKrameriusInstanceInfo = async () => {
			try {
				const response = await fetch('/api/info/kramerius')

				const json: InstanceInfo = await response.json()

				setData(json)
			} catch (e) {
				console.log(e)
			}
		}

		getKrameriusInstanceInfo()
	}, [])

	return {
		instance: data?.instance,
		url: data?.url,
	}
}
