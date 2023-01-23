import { DataGrid, GridColumns } from '@mui/x-data-grid'
import { DomainObject } from 'models/domain/domain-object'

export interface CustomGridProps<T extends DomainObject> {
	columns: GridColumns<T>
	rowCount?: number
	rows: T[]
	onPageChange: (page: number) => void
}

export const CustomGrid = <T extends DomainObject>({
	columns,
	rowCount,
	rows,
	onPageChange,
}: CustomGridProps<T>) => {
	return (
		<DataGrid
			autoHeight
			checkboxSelection={true}
			columns={columns}
			density="compact"
			disableColumnFilter
			disableColumnMenu
			disableSelectionOnClick
			getRowClassName={() => 'data-grid-row'}
			pageSize={10}
			paginationMode="server"
			rowCount={rowCount}
			rows={rows}
			rowsPerPageOptions={[]}
			sortingMode="server"
			onPageChange={onPageChange}
		/>
	)
}
