import { StepError } from './step-error'

export interface StepExecution {
	id: number
	stepName: string
	status: string
	readCount: number
	writeCount: number
	commitCount: number
	rollbackCount: number
	readSkipCount: number
	processSkipCount: number
	writeSkipCount: number
	filterCount: number
	startTime?: Date
	endTime?: Date
	exitCode: string
	exitDescription: string
	duration?: number
	errors: StepError[]
}
