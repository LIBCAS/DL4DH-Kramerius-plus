import { DataGrid, GridColDef, GridPaginationModel } from '@mui/x-data-grid'
import { GridRowSelectionModel } from '@mui/x-data-grid/models/gridRowSelectionModel'
import { DomainObject } from 'models/domain/domain-object'
import { QueryResults } from 'models/query-results'
import { JSXElementConstructor } from 'react'

export interface CustomGridProps<T extends DomainObject> {
	columns: GridColDef<T>[]
	data?: QueryResults<T>
	isLoading?: boolean
	checkboxSelection?: boolean
	pagination: GridPaginationModel
	onPaginationChange: (pagination: GridPaginationModel) => void
	selection: GridRowSelectionModel
	onSelectionChange: (selection: GridRowSelectionModel) => void
	toolbar?: JSXElementConstructor<any>
	toolbarProps?: any
}

export const CustomGrid = <T extends DomainObject>({
	columns,
	isLoading,
	data,
	pagination,
	onPaginationChange,
	selection,
	onSelectionChange,
	checkboxSelection,
	toolbar,
	toolbarProps,
}: CustomGridProps<T>) => {
	return (
		<DataGrid
			autoHeight
			checkboxSelection={checkboxSelection}
			columns={columns}
			density="compact"
			disableColumnFilter
			disableColumnMenu
			keepNonExistentRowsSelected
			loading={isLoading}
			localeText={{
				MuiTablePagination: {
					labelRowsPerPage: 'Počet řádků na stránce',
					labelDisplayedRows: ({ from, to, count }) =>
						`${from} - ${to} z ${count}`,
				},
			}}
			pageSizeOptions={[10, 20, 50]}
			paginationMode="server"
			paginationModel={pagination}
			rowCount={data?.total ?? 0}
			rowSelectionModel={selection}
			rows={data?.items ?? []}
			slotProps={{
				toolbar: toolbarProps,
			}}
			slots={{
				toolbar: toolbar,
			}}
			sortingMode="server"
			onPaginationModelChange={isLoading ? undefined : onPaginationChange}
			onRowSelectionModelChange={onSelectionChange}
		/>
	)
}
