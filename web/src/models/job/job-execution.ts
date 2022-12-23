import { StepExecution } from 'models'
import { MapType } from 'models/map-type'
import { JobParameter } from './job-parameter'

export interface JobParameters {
	[name: string]: JobParameter
}

export interface JobExecution {
	id: number
	stepExecutions: StepExecution[]
	status: string
	startTime?: Date
	createTime?: Date
	endTime?: Date
	lastUpdated?: Date
	exitCode: string
	exitDescription: string
	jobConfigurationName: string
	duration?: number
	jobParameters: MapType
}
