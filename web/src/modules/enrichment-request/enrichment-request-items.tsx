import { Box } from '@mui/material'
import {
	DataGrid,
	GridRowParams,
	GridValueFormatterParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { PageBlock } from 'components/page-block'
import { DigitalObjectModelMapping } from 'enums/publication-model'
import { EnrichmentRequestItem } from 'models/request/enrichment-request-item'
import { RequestStateMapping } from 'models/request/request'
import { FC } from 'react'

const columns = [
	{
		field: 'publicationId',
		headerName: 'UUID Publikace',
		width: 330,
	},
	{
		field: 'publicationTitle',
		headerName: 'Název publikace',
		width: 190,
	},
	{
		field: 'model',
		headerName: 'Model',
		width: 160,
		valueFormatter: (params: GridValueFormatterParams<string>) => {
			return DigitalObjectModelMapping[params.value]
		},
	},
	{
		field: 'state',
		headerName: 'Stav',
		flex: 1,
		valueGetter: (params: GridValueGetterParams) => {
			const item = params.row as EnrichmentRequestItem
			const finished = item.enrichmentChains.filter(
				chain => chain.state === 'COMPLETED',
			).length
			return `${RequestStateMapping[item.state]} (${finished}/${
				item.enrichmentChains.length
			})`
		},
	},
]

export const EnrichmentRequestItems: FC<{
	items: EnrichmentRequestItem[]
	onItemClick: (itemId: string) => void
}> = ({ items, onItemClick }) => {
	const onRowClick = (params: GridRowParams) => {
		onItemClick(params.row.id)
	}

	return (
		<PageBlock title="Položky žádosti">
			<Box display="flex" flexDirection="column" height={320}>
				<DataGrid
					columnHeaderHeight={50}
					columns={columns}
					density="compact"
					disableColumnFilter
					disableColumnMenu
					getRowClassName={() => 'data-grid-row'}
					getRowId={row => row.id}
					hideFooter
					rowHeight={50}
					rows={items}
					sx={{ flexGrow: 1 }}
					onRowClick={onRowClick}
				/>
			</Box>
		</PageBlock>
	)
}
