import { ParameterType } from 'models/parameter-type'

export interface JobParameter {
	parameter: string | Date | number
	parameterType: ParameterType
}
