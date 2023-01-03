import { Grid } from '@mui/material'
import { KeyGridItem } from 'components/key-grid-item'
import { PageBlock } from 'components/page-block'
import { ValueGridItem } from 'components/value-grid-item'
import { EnrichmentRequest } from 'models/request/enrichment-request'
import { RequestStateMapping } from 'models/request/request'
import { FC } from 'react'
import { Link } from 'react-router-dom'
import { formatDateTime } from 'utils/formatters'

type Props = {
	request: EnrichmentRequest
}

export const EnrichmentRequestInfo: FC<Props> = ({ request }) => {
	return (
		<PageBlock title="Základní informace">
			<Grid container spacing={0.5}>
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
				<KeyGridItem>Stav žádosti</KeyGridItem>
				<ValueGridItem>{RequestStateMapping[request.state]}</ValueGridItem>
			</Grid>
		</PageBlock>
	)
}
