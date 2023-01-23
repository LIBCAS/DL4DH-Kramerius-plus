import {
	DataGrid,
	GridCallbackDetails,
	GridColumns,
	GridSelectionModel,
} from '@mui/x-data-grid'
import { DomainObject } from 'models/domain/domain-object'

export interface CustomGridProps<T extends DomainObject> {
	columns: GridColumns<T>
	rowCount?: number
	rows: T[]
	onPageChange: (page: number) => void
	onSelectionChange?: (
		selectionModel: GridSelectionModel,
		details: GridCallbackDetails,
	) => void
}

export const CustomGrid = <T extends DomainObject>({
	columns,
	rowCount,
	rows,
	onPageChange,
	onSelectionChange,
}: CustomGridProps<T>) => {
	return (
		<DataGrid
			autoHeight
			checkboxSelection={true}
			columns={columns}
			density="compact"
			disableColumnFilter
			disableColumnMenu
			pageSize={10}
			paginationMode="server"
			rowCount={rowCount}
			rows={rows}
			rowsPerPageOptions={[]}
			sortingMode="server"
			onPageChange={onPageChange}
			onSelectionModelChange={onSelectionChange}
		/>
	)
}
