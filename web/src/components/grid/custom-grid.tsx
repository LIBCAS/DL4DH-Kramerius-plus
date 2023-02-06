import {
	DataGrid,
	GridCallbackDetails,
	GridColumns,
	GridInputSelectionModel,
	GridSelectionModel,
} from '@mui/x-data-grid'
import { DomainObject } from 'models/domain/domain-object'
import { JSXElementConstructor } from 'react'

export interface CustomGridProps<T extends DomainObject> {
	columns: GridColumns<T>
	rowCount?: number
	rows: T[]
	onPageChange: (page: number) => void
	onSelectionChange?: (
		selectionModel: GridSelectionModel,
		details: GridCallbackDetails,
	) => void
	selectionModel?: GridInputSelectionModel
	checkboxSelection?: boolean
	toolbar?: JSXElementConstructor<any>
	toolbarProps?: any
}

export const CustomGrid = <T extends DomainObject>({
	columns,
	rowCount,
	rows,
	onPageChange,
	onSelectionChange,
	selectionModel,
	checkboxSelection,
	toolbar,
	toolbarProps,
}: CustomGridProps<T>) => {
	return (
		<DataGrid
			autoHeight
			checkboxSelection={checkboxSelection}
			columns={columns}
			components={{
				Toolbar: toolbar,
			}}
			componentsProps={{
				toolbar: toolbarProps,
			}}
			density="compact"
			disableColumnFilter
			disableColumnMenu
			keepNonExistentRowsSelected
			pageSize={10}
			paginationMode="server"
			rowCount={rowCount}
			rows={rows}
			rowsPerPageOptions={[]}
			selectionModel={selectionModel}
			sortingMode="server"
			onPageChange={onPageChange}
			onSelectionModelChange={onSelectionChange}
		/>
	)
}
