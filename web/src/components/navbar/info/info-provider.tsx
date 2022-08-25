import { InstanceInfo } from 'models'
import { FC, useEffect, useState } from 'react'
import { InfoContext } from './info-context'

export const InfoProvider: FC<{ children: JSX.Element }> = ({ children }) => {
	const [info, setInfo] = useState<InstanceInfo | null>(null)

	useEffect(() => {
		const fetchInfo = async () => {
			try {
				const response = await fetch('/api/info')

				const json: InstanceInfo = await response.json()

				setInfo(json)
			} catch (e) {
				console.log(e)
			}
		}
		fetchInfo()
	}, [])

	return (
		<InfoContext.Provider value={{ info }}>{children}</InfoContext.Provider>
	)
}
