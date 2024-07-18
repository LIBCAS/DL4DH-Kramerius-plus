import { DataGrid, GridColDef } from '@mui/x-data-grid'
import { PageBlock } from 'components/page-block'
import { UserRequestStateAuditDto } from 'models/user-requests'

export const UserRequestStateChanges = ({
	stateChanges,
}: {
	stateChanges: UserRequestStateAuditDto[]
}) => {
	const cols: GridColDef[] = [
		{
			field: 'created',
			headerName: 'Čas',
			width: 400,
			valueFormatter: ({ value }) => value.toLocaleString(),
		},
		{ field: 'username', headerName: 'Změnu provedl', width: 200 },
		{ field: 'before', headerName: 'Stav před', width: 200 },
		{ field: 'after', headerName: 'Stav po', width: 200 },
	]

	return (
		<PageBlock title={'Historie změn'}>
			{stateChanges && (
				<DataGrid
					columns={cols}
					getRowId={row => row.created}
					rows={stateChanges}
				/>
			)}
		</PageBlock>
	)
}
