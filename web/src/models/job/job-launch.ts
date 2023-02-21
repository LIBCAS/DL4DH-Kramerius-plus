export interface LastLaunch {
	timestamp: Date
	launchStatus: 'SUCCESS' | 'FAILED'
	message?: string
}
