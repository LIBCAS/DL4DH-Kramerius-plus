import { ExitStatus } from '../exit-status'

export interface StepExecution {
	stepName: string
	status: string
	startTime?: Date
	endTime?: Date
	exitStatus: ExitStatus
	duration?: number
}
