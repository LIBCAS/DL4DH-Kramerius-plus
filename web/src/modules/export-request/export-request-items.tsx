import {
	Box,
	Paper,
	Table,
	TableBody,
	TableCell,
	TableContainer,
	TableHead,
	TableRow,
} from '@mui/material'
import { PageBlock } from 'components/page-block'
import { Export, FileRef } from 'models'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { FC } from 'react'
import { ExportRequestItemRow } from './export-request-item-row'

export type ExportRowType = {
	id: string
	publicationId?: string
	publicationTitle?: string
	model?: string
	state?: string
	fileRef?: FileRef
	exportJob?: KrameriusJobInstance
	children?: Export[]
}

export const ExportRequestItems: FC<{
	items: ExportRowType[]
}> = ({ items }) => {
	return (
		<PageBlock title="Položky žádosti">
			{!!items.length && (
				<Box display="flex" flexDirection="column" height={320}>
					<TableContainer component={Paper} variant="outlined">
						<Table size="small" stickyHeader>
							<TableHead>
								<TableRow>
									<TableCell></TableCell>
									<TableCell>UUID Publikace</TableCell>
									<TableCell>Název publikace</TableCell>
									<TableCell>Model</TableCell>
									<TableCell>Stav</TableCell>
									<TableCell># Dílčích exportů</TableCell>
									<TableCell>Exportovací úloha</TableCell>
									<TableCell>Soubor</TableCell>
								</TableRow>
							</TableHead>
							<TableBody>
								{items.map(item => (
									<ExportRequestItemRow key={item.id} depth={0} row={item} />
								))}
							</TableBody>
						</Table>
					</TableContainer>
				</Box>
			)}
		</PageBlock>
	)
}
