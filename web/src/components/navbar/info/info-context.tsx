import { InstanceInfo } from 'models'
import { createContext, useContext } from 'react'

interface InfoContextInterface {
	info: InstanceInfo | null
}

export const InfoContext = createContext<InfoContextInterface>({
	info: null,
})

export const useInfo = () => useContext(InfoContext)
