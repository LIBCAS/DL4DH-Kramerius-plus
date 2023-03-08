import { DataGrid, GridColDef, GridPaginationModel } from '@mui/x-data-grid'
import { GridRowSelectionModel } from '@mui/x-data-grid/models/gridRowSelectionModel'
import { DomainObject } from 'models/domain/domain-object'
import { QueryResults } from 'models/query-results'
import { JSXElementConstructor, useEffect, useState } from 'react'

export interface CustomGridProps<T extends DomainObject> {
	columns: GridColDef<T>[]
	data?: QueryResults<T>
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
	data,
	pagination,
	onPaginationChange,
	selection,
	onSelectionChange,
	checkboxSelection,
	toolbar,
	toolbarProps,
}: CustomGridProps<T>) => {
	const [rowCountState, setRowCountState] = useState<number>(
		pagination.pageSize,
	)

	useEffect(() => {
		setRowCountState(prevRowCountState =>
			data?.total !== undefined ? data.total : prevRowCountState,
		)
	}, [data, setRowCountState])

	return (
		<DataGrid
			autoHeight
			checkboxSelection={checkboxSelection}
			columns={columns}
			density="compact"
			disableColumnFilter
			disableColumnMenu
			keepNonExistentRowsSelected
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
			rowCount={rowCountState}
			rowSelectionModel={selection}
			rows={data?.items ?? []}
			slotProps={{
				toolbar: toolbarProps,
			}}
			slots={{
				toolbar: toolbar,
			}}
			sortingMode="server"
			onPaginationModelChange={onPaginationChange}
			onRowSelectionModelChange={onSelectionChange}
		/>
	)
}
