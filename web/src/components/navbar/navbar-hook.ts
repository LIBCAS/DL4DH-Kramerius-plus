import { InstanceInfo } from 'models'
import { MapType } from 'models/map-type'
import { useEffect, useState } from 'react'

import fetch from 'utils/fetch'

export const useNavbar = () => {
	const [data, setData] = useState<InstanceInfo>()
	const [versionInfo, setVersionInfo] = useState<MapType>()

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

		const getKrameriusPlusInfo = async () => {
			try {
				const response = await fetch('/api/info/version')

				const json: MapType = await response.json()

				setVersionInfo(json)
			} catch (e) {
				console.log(e)
			}
		}

		getKrameriusInstanceInfo()
		getKrameriusPlusInfo()
	}, [])

	return {
		instance: data?.info.name,
		url: data?.info.url,
		version: versionInfo?.version,
	}
}
