import { Grid } from '@mui/material'
import { KeyGridItem } from 'components/key-grid-item'
import { ValueGridItem } from 'components/value-grid-item'
import { UserRequestDetailDto } from 'models/user-requests'

export const UserRequestDetailInfo = ({
	request,
}: {
	request: UserRequestDetailDto
}) => {
	return (
		<Grid>
			<KeyGridItem>UUID</KeyGridItem>
			<ValueGridItem>{request.id}</ValueGridItem>
			<KeyGridItem>Identifikace</KeyGridItem>
			<ValueGridItem>{request.identification}</ValueGridItem>
			<KeyGridItem>Vytvořeno</KeyGridItem>
			<ValueGridItem>{request.created.toUTCString()}</ValueGridItem>
			<KeyGridItem>Změneno</KeyGridItem>
			<ValueGridItem>{request.updated.toUTCString()}</ValueGridItem>
			<KeyGridItem>Užívatel</KeyGridItem>
			<ValueGridItem>{request.username}</ValueGridItem>
		</Grid>
	)
}
