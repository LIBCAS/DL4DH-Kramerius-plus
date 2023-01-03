import { Grid } from '@mui/material'
import { KeyGridItem } from 'components/key-grid-item'
import { PageBlock } from 'components/page-block'
import { ValueGridItem } from 'components/value-grid-item'
import { ExportRequest } from 'models/request/export-request'
import { FC } from 'react'
import { Link } from 'react-router-dom'
import { formatDateTime } from 'utils/formatters'

export const ExportRequestInfo: FC<{ request: ExportRequest }> = ({
	request,
}) => {
	return (
		<PageBlock title="Základní informace">
			<Grid container spacing={1}>
				<KeyGridItem>Autor</KeyGridItem>
				<ValueGridItem>{request.owner.username}</ValueGridItem>
				<KeyGridItem>Dátum vytvoření</KeyGridItem>
				<ValueGridItem>{formatDateTime(request.created)}</ValueGridItem>
				<KeyGridItem>Název</KeyGridItem>
				<ValueGridItem>{request.name ?? '-'}</ValueGridItem>
				<KeyGridItem>Inicializační úloha</KeyGridItem>
				<ValueGridItem>
					<Link to={`/jobs/${request.createRequestJob.id}`}>
						{request.createRequestJob.executionStatus}
					</Link>
				</ValueGridItem>
			</Grid>
		</PageBlock>
	)
}
