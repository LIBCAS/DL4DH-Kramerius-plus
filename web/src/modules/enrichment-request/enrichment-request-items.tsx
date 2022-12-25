import { Box } from '@mui/material'
import {
	DataGrid,
	GridRowParams,
	GridValueFormatterParams,
} from '@mui/x-data-grid'
import { PageBlock } from 'components/page-block'
import { DigitalObjectModelMapping } from 'enums/publication-model'
import { EnrichmentChain } from 'models/request/enrichment-chain'
import { EnrichmentRequestItem } from 'models/request/enrichment-request-item'
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
		width: 280,
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
		field: 'enrichmentChains',
		headerName: '# Plánů',
		valueFormatter: (params: GridValueFormatterParams<EnrichmentChain[]>) => {
			return params.value.length
		},
		type: 'number',
		flex: 1,
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
					columns={columns}
					density="compact"
					disableColumnFilter
					disableColumnMenu
					getRowClassName={() => 'data-grid-row'}
					getRowId={row => row.id}
					headerHeight={50}
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
