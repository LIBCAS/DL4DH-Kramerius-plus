import { ParameterType } from './parameter-type'

export interface JobParameter {
	parameter: string | Date | number
	parameterType: ParameterType
}
