/**
 * DO NOT USE this enum to deserialize values from BE, not all values are in this enum. Use only
 * for filtering options
 */
export enum JobStatus {
	CREATED = 'CREATED',
	COMPLETED = 'COMPLETED',
	STARTED = 'STARTED',
	STOPPED = 'STOPPED',
	FAILED = 'FAILED',
}
