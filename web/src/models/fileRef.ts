import { z } from 'zod'

export interface FileRef {
	id?: string
	created?: string
	name?: string
	contentType?: string
	size?: number
}

export const fileRefSchema = z.object({
	id: z.string(),
	created: z.string(),
	name: z.string(),
	contentType: z.string(),
	size: z.number().optional(),
})

export type FileRefStrict = z.infer<typeof fileRefSchema>
