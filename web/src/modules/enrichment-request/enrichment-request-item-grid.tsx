import { TableCell } from '@material-ui/core'
import {
	Paper,
	Table,
	TableBody,
	TableContainer,
	TableHead,
	TableRow,
} from '@mui/material'
import { EnrichmentRequestItem } from 'models/request/enrichment-request-item'
import { FC } from 'react'
import { EnrichmentRequestItemRow } from './enrichment-request-item-row'

type Props = {
	items: EnrichmentRequestItem[]
}

export const EnrichmentRequestItemGrid: FC<Props> = ({ items }) => {
	return (
		<TableContainer component={Paper} variant="outlined">
			<Table size="small">
				<TableHead>
					<TableRow>
						<TableCell></TableCell>
						<TableCell>UUID Publikace</TableCell>
						<TableCell>Název publikace</TableCell>
						<TableCell>Typ publikace</TableCell>
						<TableCell>Počet podpublikací</TableCell>
					</TableRow>
				</TableHead>
				<TableBody>
					{items.map(item => (
						<EnrichmentRequestItemRow key={item.id} item={item} />
					))}
				</TableBody>
			</Table>
		</TableContainer>
	)
}
