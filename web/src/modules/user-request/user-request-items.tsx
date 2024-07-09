import {
	Box,
	Paper,
	Table,
	TableBody,
	TableCell,
	TableContainer,
	TableHead,
	TableRow,
} from '@mui/material'
import { PageBlock } from 'components/page-block'
import { UserRequestPartDto } from 'models/user-requests'
import { UserRequestPartRow } from './user-request-part-row'

export const UserRequestItems = ({
	items,
	requestId,
}: {
	items: UserRequestPartDto[]
	requestId: string
}) => {
	return (
		<PageBlock title="Položky žádosti">
			{items && (
				<Box
					component={Paper}
					display="flex"
					flexDirection="column"
					height={320}
				>
					<TableContainer component={Paper} variant="outlined">
						<Table size="small" stickyHeader>
							<TableHead>
								<TableRow>
									<TableCell width={320}>UUID Publikace</TableCell>
									<TableCell width={100}>Stav</TableCell>
									<TableCell width={350}>Poznámka</TableCell>
									<TableCell width={250}>Stav do</TableCell>
									<TableCell width={250}>Akce</TableCell>
								</TableRow>
							</TableHead>
							<TableBody>
								{items.map(item => (
									<UserRequestPartRow
										key={item.publicationId}
										part={item}
										requestId={requestId}
									/>
								))}
							</TableBody>
						</Table>
					</TableContainer>
				</Box>
			)}
		</PageBlock>
	)
}
