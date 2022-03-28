import { StepExecution } from '.'
import { ExitStatus } from './exit-status'
import { JobParameter } from './job-parameter'

export interface JobParameters {
	[name: string]: JobParameter
}

export interface JobExecution {
	id: number
	stepExecutions: StepExecution[]
	status: string
	startTime?: Date
	endTime?: Date
	exitStatus: ExitStatus
	jobConfigurationName: string
	duration?: number
	jobParameters: JobParameters
}
